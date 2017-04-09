(ns sokoban.bsp.helpers
  (:require [cgsx.tools.matcher :refer :all]
            [sokoban.helpers.helpers :refer :all]))

(defn has-walls?
  "Checks if a zone has any walls remaining."
  [world]
  (some (fn [x] (= 0 (:type x))) world))

(def no-walls? (complement has-walls?))

(defn only-walls?
  "Checks if a zone has only walls (in other words, no floors of any kind."
  [world]
  (not-any? (fn [x] (not= 0 (:type x))) world))

(defn switch-dir
  "Alternates between axes."
  [direction]
  (cond
   (= :x direction) :y
   (= :y direction) :x))

(defn count-tiles
  "Counts the total tiles of a specified type in a slice."
  [slice type]
  (count (remove false? (map (fn [x] (= (:type x) type)) slice)))
  )

(defn get-edge
  "Return a specified edge."
  [zone edge]
  (edge (mlet ['(zone (?left ?top) (?right ?bottom)) zone]
              (mout '{:left ?left :top ?top :right ?right :bottom ?bottom}))))

(defn compare-edge
  "Determine the distance between two edges."
  [first-edge second-edge]
  (abs (- first-edge second-edge)))

(defn edges-adjacent?
  "Are the given edges on adjacent rows or columns?"
  [first-edge second-edge]
  (= 1 (compare-edge first-edge second-edge)))
