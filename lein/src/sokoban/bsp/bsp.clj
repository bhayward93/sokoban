(ns sokoban.bsp.bsp
  (:require [sokoban.helpers.helpers :refer :all]
            [sokoban.bsp.helpers :refer :all]))

(defn get-map-bounds
  "Returns the highest or lowest x or y co-ordinate available within the given map."
  [world direction extreme]
  (reduce extreme (map direction world))
)

(defn get-slice
  "Obtains the specified row or column from the map."
  [world direction val]
  (remove nil? (map (fn [x] (if (= val (direction x)) x)) world))
  )

(defn get-zone-slice
  [world direction zone zone-edge]
  (let [other-direction (switch-dir direction)
        first-end (if (= :x other-direction)
                    (get-edge zone :left)
                    (get-edge zone :top))
        other-end (if (= :x other-direction)
                    (get-edge zone :right)
                    (get-edge zone :bottom))]
    (remove nil? (map (fn [x] 
                        (if (and
                             (>= (other-direction x) first-end)
                             (<= (other-direction x) other-end)) x))
                      (get-slice world direction zone-edge)))))

(defn get-tile-difference
  "Compares differences between the counts of tiles of a specified type in two slices."
  [world direction type first-slice second-slice]
  (abs (reduce - (map (fn [x] 
                        (count-tiles (get-slice world direction x) type))
                      (list first-slice second-slice)))))

(defn get-tile-difference-list
  "Obtains a list of tile count differences across a specified axis."
  [world direction type]
  (map (fn [x] 
         {direction x :diff (get-tile-difference world direction type (dec x) x)})
       (range (inc (get-map-bounds world direction min))
              (inc (get-map-bounds world direction max))))
  )

(defn get-central-bound
  "Finds the first boundary in a given list with a non-zero tile difference."
  [list]
  (loop
    [remaining-list list]
    (cond
      (empty? remaining-list) nil
      (not= (:diff (first remaining-list)) 0) (first remaining-list)
      :else (recur (rest remaining-list))
      )
    )
  )

(defn split-list
  "Splits a list of tile differences half"
  [world direction type]
   (split-with
     (complement 
       (fn [x] 
         (< (/ (get-map-bounds world direction max) 2) 
         (direction x))))
     (get-tile-difference-list world direction type)
     )
  )

(defn get-central-bounds
  [world direction type]
  (remove nil? (list
                (get-central-bound (reverse (first (split-list world direction type))))
                (get-central-bound (last (split-list world direction type))))))

(defn get-all-central-bounds
  [world type]
  (concat
   (get-central-bounds world :x type)
   (get-central-bounds world :y type)))

(defn get-largest-bound
  "Returns the largest difference in rows or columns between tiles of specified
  type and those that do not match that type."
  ([world type]
   (reduce max (map 
                :diff
                (get-all-central-bounds world type))))
  ([world type direction]
   (reduce max (map
                :diff
                (get-central-bounds world direction type)))))

(defn get-splitting-line
  ([world type]
   (first (remove nil?
                  (map
                   (fn [x] (if (= (:diff x) (get-largest-bound world type)) x))
                   (get-all-central-bounds world type)))))
  ([world type direction]
   (first (remove nil?
                  (map
                   (fn [x] (if (= (:diff x) (get-largest-bound world type direction)) x))
                   (get-central-bounds world direction type))))))

(defn find-splitting-line
  [world type direction]
  (let [splitting-line (get-splitting-line world type direction)]
    (if (nil? splitting-line)
      (get-splitting-line world type (switch-dir direction))
      splitting-line)))

(defn split-map
  [world splitting-line]
  (let [direction (first (keys splitting-line))]
    (split-with
     (fn [x] (> (direction splitting-line) (direction x))) 
     (sort-by direction world))))

(defn get-zone
  "Returns the upper-left and lower-right bounds of a zone if it is now a wall."
  [node]
  (if (> (:type (first node)) 0)
    (list 'zone 
          (list (get-map-bounds node :x min)
                (get-map-bounds node :y min))
          (list (get-map-bounds node :x max)
                (get-map-bounds node :y max)))))

(defn collect-zones
  "Builds a list of non-wall zones from a given BSP tree."
  ([node]
   (remove nil? (collect-zones node [])))
  ([node parents]
   (cond
    (empty? node) parents
    (map? (first node)) (concat parents (conj () (get-zone node)))
    :else (concat parents
                  (collect-zones (first node) parents)
                  (collect-zones (rest node) parents)))))

(defn collect-links
  ([node]
   (collect-links node []))
  ([node parents]
   (cond
    (empty? node) parents
    (vector? node) (list node)
    :else (mapcat (fn [x] (collect-links x parents)) node))))

(defn links-into-lists
  [set]
  (map (fn [x] (apply list x)) set))

(defn bsp
  "Divides a map into zones using a form of binary space partitioning."
  ([world]
   (cond
    (empty? world) world
    (no-walls? world) world
    (only-walls? world) world
    :else (let [splitting-line (get-splitting-line world 0)
                direction (first (keys splitting-line))
                new-map (split-map world splitting-line)]
            (map (fn [x] (bsp x (switch-dir direction))) new-map))))
  ([world direction]
   (cond
    (empty? world) world
    (no-walls? world) world
    (only-walls? world) world
    :else (let [splitting-line (find-splitting-line world 0 direction)
                direction (first (keys splitting-line))
                new-map (split-map world splitting-line)]
            (map (fn [x] (bsp x (switch-dir direction))) new-map)))))

(defn add-links
  [world
   first-zone first-zone-edge
   next-zone next-zone-edge]
  (if (edges-adjacent? (get-edge first-zone first-zone-edge)
                       (get-edge next-zone next-zone-edge))
    (let [direction (if (or (= first-zone-edge :left) (= first-zone-edge :right)) :x :y)
          first-zone-slice (get-zone-slice world
                                           direction
                                           first-zone
                                           (get-edge first-zone first-zone-edge))
          next-zone-slice (get-zone-slice world
                                          direction
                                          next-zone
                                          (get-edge next-zone next-zone-edge))]
      (remove empty? (map (fn [x]
                            (remove nil? (map (fn [y]
                                                (if (= ((switch-dir direction) x)
                                                       ((switch-dir direction) y))
                                                  (vector
                                                   'links
                                                   (list (:x x) (:y x))
                                                   (list (:x y) (:y y)))))
                                              next-zone-slice)))
                          first-zone-slice)))))

(defn connect-zones
  ([world zones]
   (if (empty? zones)
     zones
     (let [first-zone (first zones)
           remaining-zones (rest zones)]
       (remove empty? (concat (map
                               (fn [x] (connect-zones world first-zone x))
                               remaining-zones) (connect-zones world (rest zones)))))))
  ([world first-zone next-zone]
   (remove #(or (nil? %) (empty? %)) (list
                                      (add-links world first-zone :left
                                                 next-zone :right)
                                      (add-links world first-zone :right 
                                                 next-zone :left)
                                      (add-links world first-zone :top
                                                 next-zone :bottom)
                                      (add-links world first-zone :bottom
                                                 next-zone :top)))))
