module Crash (printCrash) where
  
-- concate of strings
_a :: [a] -> [a] -> [a] -> [a]
_a x y z = x ++ y ++ z

-- addition two numbers
_b :: Int -> Int -> Int
_b x y = x + y

-- divide two numbers
_c :: Float -> Float -> Float
_c x y = x / y

-- guard expression
_d :: Int -> Int -> Bool
_d x y
  | x > y = True
  | x == y = True
  | otherwise = False

-- if expressions
_e1 :: Float -> Float -> Bool
_e1 x y = if x >= y then True else False

-- case expression
_e2 :: [a] -> String
_e2 _x = "The list is " ++ case _x of
  [] -> "empty."
  [_a] -> "singleton list."
  _x -> "a longer list."

-- let expression
_e3 :: Float -> Float -> Float
_e3 a b = let
  -- create new value inside function that can be used in `in` scope for final `=` part
  addition = a + b
  substraction = a - b
  in
    addition * substraction

-- use where for manipulate inputs value before proceed in `=` part
_f1 :: String -> String -> String
_f1 firstname lastname = [f] ++ ". " ++ [l] ++ "."
  where f = head firstname -- in `f` is now first letter from `firstname` value
        l = head lastname -- in `l` is now first letter from `lastname` value

-- same concept as before but rewrited with let and in 
_f2 :: String -> String -> String
_f2 firstname lastname = let
  f = head firstname
  l = head lastname
  in [f] ++ ". " ++ [l] ++ "."

-- pattern matching
_g :: Int -> String
_g 1 = "One"
_g 2 = "Two"
_g 12 = "One" ++ "Two"
_g x = "Value: " ++ (show(x))

-- recursion for factorial (pattern matching)
_h1 :: Int -> Int
_h1 1 = 1
_h1 0 = 0
_h1 x = x * _h1 (x - 1)

-- recursion for factorial (guard expression)
_h2 :: Int -> Int
_h2 x
  | x == 0 = 0
  | x == 1 = 1
  | otherwise = x * _h2 (x - 1)

-- loop (addition numbers between interval)
_i :: Int -> Int -> Int
_i from to = do
  loop from where -- `from` is `from` from the function parameters
    loop _from = do -- `_from` is value changing in each recursion, init value is `from
      if _from /= to
      then loop (_from + 1) + _from
      else to
      
-- read input
_j :: IO ()
_j = do
  putStrLn "Write something..."
  name <- getLine
  putStrLn ("You wrote " ++ name)
      
printCrash :: IO ()      
printCrash = do
    putStrLn (show(_a "Hello" " " "World"))
    putStrLn (show(_b 5 10))
    putStrLn (show(_c 5 10))
    putStrLn (show(_d 11 10))
    putStrLn (show(_e1 10.8 10.9))
    putStrLn (show(_e2 ["s", "a"]))
    putStrLn (show(_e3 78 4))
    putStrLn (show(_f2 "John" "Petterson"))
    putStrLn (show(_g 3))
    putStrLn (show(_h1 10))
    putStrLn (show(_h2 10))
    putStrLn (show(_i 5 10))
