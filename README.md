# RayCasting
ray casting psuedo-3d engine in java

## TODO
  * Have a camera class with pos vector, dir vector, and plane vector. I think you can calculate a plane vector given a fov with the dot product. Basically take in a starting angle and fov -> create a unit vector with the angle and that's the direction, then the plane vector has to be perpendicular but the size can be changed to make the fov. This could probably be easily generalized by (x',y') -> (-y,x) for a 90 degree rotation. Then i could multiply y' by the fov / 100 (to normalize it)
  * Have a worldmap class to store information about the 2D-world. 
  * Implement a render distance, better collision detection, mouse control, and texture mapping. It should be possible to have a world that is not walled off and for the engine to not break. 
  * It is time to make an engine library so i don't have to keep copying my code. 
