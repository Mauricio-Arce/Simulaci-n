import System.IO
import System.Random
import Data.Char
import Text.Printf

ciclosRandom 1000000 = return()
ciclosRandom n = do
    numeroAleatorio <- randomRIO(0.0::Double, 1.0::Double)	    
    appendFile "randomHaskell.txt" (printf "%.15f" numeroAleatorio ++ "\n")
    ciclosRandom(n+1)

main = do
    archivo <- openFile "/home/mauricio/Escritorio/Simulación/Tarea2/Pruebas/randomHaskell.txt" WriteMode
    hSetFileSize archivo 0 --Eliminan todos los datos del archivo
    hClose archivo
    ciclosRandom(0)

    
