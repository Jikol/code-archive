{-# LANGUAGE FlexibleContexts #-}

module Sandbox (printSandbox) where

_fct :: Int -> Int
_fct num = do
  loop num where
    loop 1 = 1
    loop 0 = 0
    loop _num = _num * loop (_num - 1)


_fib :: IO ()
_fib = do
  input where -- here `input` will be executed as a result
    loop :: (Integer, Integer, Integer, Integer) -> String
    loop (a, b, n, m) = do
      if n > 0
      then loop (b, (a + b), (n - 1), m)
      else (show m) ++ "-th fibonacci number is " ++ (show a)
      
    input :: IO ()    
    input = do
      putStrLn "What n-th fibonacci number you wanted?"
      val <- getLine
      let num = (read val :: Integer)
      print (loop (1, 1, num, num))
       
_max2 :: Ord a => (a, a) -> a
_max2 (a, b) = do
  if a > b then a else b
    
_comb :: (Int, Int) -> Int
_comb (a, b) = let
  v = _fct a
  x = _fct b
  y = _fct (a - b)
  z = x * y
  in (div v z)
  
_sum :: Num t => [t] -> t
_sum list = do
  loop list 0 where
    loop [] _summ = _summ
    loop (x:xs) _summ = loop xs (_summ + x)
  
_contain :: Eq a => (a, [a]) -> Bool
_contain (a, list) = do
  loop list where
    loop [] = False
    loop (x:xs) = do
      if x == a
      then True
      else loop xs

_gen :: Integral b => [b] -> [(b, b)]
_gen list = [(x, x * 2) | x <- list, mod x 2 == 0]

_odd :: Integral a => (a, a) -> [a]
_odd (a, b) = [(x) | x <- [a..b], odd x]

_text :: String
_text = "fef fwef wfw ewf w  tr hre hh rhrh trhrer hr hr h rh rh rth tr h rth fw we few"

_fil1 :: String -> [String]
_fil1 list = [ x | x <- (words list), length x <= 2] 

_fil2 :: String -> [String]
_fil2 list = filter (\x -> length x <= 2) (words list)


type Pic = [String]

pic :: Pic
pic = ["....#....",
       "...###...",
       "..#.#.#..",
       ".#..#..#.",
       "....#....",
       "....#....",
       "....#####"]

_pp :: Pic -> IO ()
_pp x = loop x where loop [] = putStrLn ""; loop (_x:_xs) = do putStrLn _x; loop _xs

_flipV :: Pic -> Pic
_flipV ll = map (\x -> reverse x) ll

printSandbox :: IO ()
printSandbox = do
  print $ _odd (5, 10)
  