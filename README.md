# MarsRover

Simple program to move the Mars Rover on a 2D grid.

## How to Run
```
sbt run
```

## How to Use

First you need to enter a grid size in the format `x,y` where x is the size of the x axis and y is the the size of the y axis for the grid

After this you can see the stats for the Rover and you can issue these commands:

- `move` to move the Rover in the direction of the Heading by one space
- `cw` to rotate the Rover clockwise
- `ccw` to rotate the Rover counter clockwise
- `exit` to exit the app

## How to Test
```
sbt test
```
