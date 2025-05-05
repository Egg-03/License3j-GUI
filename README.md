# License3j-GUI
A simple GUI app in Swing, based on Peter Verhas' [License3j](https://github.com/verhas/License3j), a free License Management Library in Java.
The GUI is based on the [License3j-REPL App](https://github.com/verhas/License3jRepl) which is a CLI Application used to create key pairs and create and sign licenses.
The typical workflow is similar to that of the REPL Application and the GUI offers additional real-time information about your keys and licenses in memory such as it's signature and save status.

# Download
You can download pre-built binaries from the [Releases](https://github.com/Egg-03/License3j-GUI/releases/latest) page

# Build
You can also build your own binaries from the source code using Maven

#### Clone the repository locally
```
git clone https://github.com/Egg-03/License3j-GUI.git
```

#### Build artifacts using Maven
In the repository directory, run the following command
```
mvn -e clean package
```

# Instructions
Upon starting the application, you will see this UI

![image](https://github.com/user-attachments/assets/b6bd72a9-9a03-485c-aea4-887c8dc25973)

The following workflows are available from this point:
1) Creating a new license
2) Loading an *already saved license*
3) Generating new keys for signing your license
4) Loading *already saved keys*

  
