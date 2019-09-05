<div align="center">
  <img src = "https://raw.githubusercontent.com/Josh194/Ai/master/FFNN/src/images/logo.png">
</div>

-----------------
**NOTE: This project has been discontinued; I started this project at a time when I had just started to learn programming. The project was build on terrible frameworks, and there were a good hundred or so various problems (Such as computation cycles being limited to the speed at which frames could be drawn on screen). It has become almost impossible to add new features due to to the aforementioned frameworks, and it would take too long to fix all the problems. Because of that, I am working on a new version of the project, in C++, with many new features, and with many of the issues in this version fixed.**

Version: 1.3.2

An open source neural network creation tool designed to give a visual into deep learning. Currently, this project only supports deep feed forward networks, but convolutional networks, recurrent networks, and other types will be added soon.

Features:
* Dynamic architecture
* Easy input through a text file
* Saving/Loading of networks through .xml files
* Realtime error graph for easy visualization
* Different activation functions
* Variable learning rate

New:
* New error graph with line simplification algorithm 
* Full support for multiple output neurons
* Neuron resizing based on layer size
* Fixed graph range
* Highlight selected connections
* Drag to create new connections
* Realtime input support
* Removed Thread.sleep() in feed loop, allowing the ai to run much faster
* Epoch counter
* Other changes

Note:
If you are having problems giving the NN input, make sure your input file ends with a new line.
