module Main (main) where

type Crossword = [String]

_pp :: Crossword -> IO ()
_pp x = do
  loop x where
    space [] = []
    space [a] = [a]
    space (_x:xs) = _x : ' ' : space xs

    loop [] = putStrLn ""
    loop (_x:_xs) = do
      putStrLn (space _x)
      loop _xs

_crossword :: Crossword
_crossword = ["...##....",
              ".#....##.",
              ".#.##....",
              "....#.#.#",
              "#.#...#.#",
              "#.#.#....",
              "....##.#.",
              ".##....#.",
              "....##.#."]

_positions :: Crossword -> [(Int, Int)]
_positions cross = let
  itered = iter cross 0 []
  counted = (map (\s -> count (fst s) 0 0 0 (snd s) []) itered)
  transformed = (map (\s -> (trans s (0 :: Int) [])) counted)
  in [y | x <- transformed, y <- x]
    where
      iter :: Num b => [a] -> b -> [(a, b)] -> [(a, b)]
      iter [] _ list = reverse list
      iter (x:xs) i list = do
        iter xs (i + 1) ((x, i) : list)

      count [] _ _ _ _ list = reverse list
      count (x:xs) dot hash i n list = do
        if x == '.'
        then count xs (dot + 1) 0 (i + 1) n ((dot, i, '.', n) : list)
        else count xs 0 (hash + 1) (i + 1) n ((hash, i, '#', n) : list)

      trans [] _ list = reverse list
      trans (x:xs) i list = let
        (a, b, c, d) = x
        in
          if c == '.' && a == 2
          then trans xs i ((d, b - a) : list)
          else trans xs i list

main :: IO ()
main = do
  putStrLn "Crossword puzzle outline"
  _pp _crossword
  putStrLn "Computed word start positions greater than 2 letters in each row and column"
  print (_positions _crossword)
  _ <- getLine
  putStrLn ""
