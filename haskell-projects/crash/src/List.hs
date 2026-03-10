module List (printList) where

-- list testing
_a :: [Int] -> String
_a list = do
  foo list where
    foo [] = "Test"
    foo (_:x) = show(length x)

-- get 3rd item from list (first attempt)
_b1 :: [Int] -> String
_b1 list = do
  get list where
    get [] = "List must have more than 3 items"
    get [_a] = "List must have more than 3 items"
    get [_a,_b] = "List must have more than 3 items"
    get (_:_:c) = show(head c)

-- get 3rd item from list (seccond attempt)
_b2 :: [Int] -> Int
_b2 list = list !! 2

-- copy first 2 items from list and create new list from it
_c :: [Int] -> IO ()
_c list = do
  let newList = take 2 list in do
    putStrLn (show(newList))
    putStrLn (show(list))

_d :: [Int] -> IO ()
_d list = do
  putStrLn (show(filter (>= 2) list))
  putStrLn (show(reverse list))
  putStrLn (show(map (* maxItem) list)) where
    maxItem = maximum list

_e :: [Int] -> [Int]
_e list = do
  let result = sum list in do
    map (* result) list

-- implementation of `zip` function
_zip :: [a] -> [b] -> [(a, b)]
_zip l1 l2 = do
  loop l1 l2 where
    loop (x:xs) (y:ys) = (x, y) : loop xs ys
    loop _ _ = []

-- convert list of int to string
_fromInt :: [Float] -> [Int]
_fromInt list = do
  loop list where
    loop (x:xs) = floor x : loop xs
    loop _ = []

-- count occurance of words in list
_count :: Int -> String -> [(String, Int)]
_count limit list = let
  allWords = words list
  keys = unique allWords
  limitKeys = longerThan keys
  in occur limitKeys allWords
    where
      unique :: [String] -> [String]
      unique [] = []
      unique (x:xs) = x : unique (filter (/= x) xs)
           
      longerThan :: [String] -> [String]
      longerThan [] = []
      longerThan (x:xs) = do
        if (length x) >= limit
        then x : longerThan xs
        else longerThan xs
      
      occur [] _ = []
      occur (x:xs) y = (x, length (filter (== x) y)) : occur xs y


printList :: IO ()
printList = do
  putStrLn (show (_a [1, 2, 3]))
  -- print is literally putStrLn and show together
  print (_a [1, 2, 3])
  -- instead of () you can use $ which will wrap next things as parameters
  print $ _a [1, 2, 3] 