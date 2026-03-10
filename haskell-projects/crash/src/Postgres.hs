{-# LANGUAGE OverloadedStrings #-}
module Postgres (postgre_print) where
  
import Database.PostgreSQL.Simple

-- connection credentials to PostgreSQL
localPG :: ConnectInfo
localPG = defaultConnectInfo { 
  connectHost = "127.0.0.1",
  connectDatabase = "postgres",
  connectUser = "postgres",
  connectPassword = "postgres" 
}

-- hello world function 
postgre_print :: IO ()
postgre_print = do
  conn <- connect localPG
  mapM_ print =<< (query_ conn "SELECT CONCAT('Hello ', 'Postgre!')" :: IO [Only 
   String])


