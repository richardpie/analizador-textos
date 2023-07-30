# analizador-textos

## How to run the progam
Download the binary [here](https://github.com/richardpie/analizador-textos/tree/main/executable)

## How to compile and build the jar file
First compile with IntelliJ and Java 11.  
Then, within the root folder of this project, execute:
```
cd /home/{user}/analizador-textos/out/production/analizador-textos
find . -name "*.class" > classes.txt
jar cfe program.jar Main.CapaPresentacio.MainPresentacio -C /home/{user}/analizador-textos/out/production/analizador-textos @classes.txt
mv program.jar ../../../program.jar
```
Remember to replace `{user}` with your user or folder where you have downloaded this repo to.
