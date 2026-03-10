module Main (main) where

x :: [Int] -> [Int]    
x list = do
  let result = sum list in do
    map (* result) list

main :: IO ()
main = do
  putStrLn (show(cycle(x(take(1000000000000000::Int)(repeat (1000000000000000::Int))))))