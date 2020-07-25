# SDI-hw-pathfinder
### Data Structure
| Program Files | |
| --- | --- |
|[Path](https://github.com/bellaroseee/SDI-hw-pathfinder/blob/master/src/main/java/pathfinder/datastructures/Path.java) | path between two buildings |
|[Point](https://github.com/bellaroseee/SDI-hw-pathfinder/blob/master/src/main/java/pathfinder/datastructures/Point.java) | a cartesian coordinate to represent the building's location on the map|

### Parser
| Program Files | |
| --- | --- |
| [CampusPathParser](https://github.com/bellaroseee/SDI-hw-pathfinder/blob/master/src/main/java/pathfinder/parser/CampusPathsParser.java) | parses the campus building file and campus path file |
| [Dijkstra](https://github.com/bellaroseee/SDI-hw-pathfinder/blob/master/src/main/java/pathfinder/parser/Dijkstra.java) | implementation of Dijkstra's algorithm to find shortest path between two buildings |

### Campus Map Model API
| Program Files | |
| --- | --- |
| [ModelAPI](https://github.com/bellaroseee/SDI-hw-pathfinder/blob/master/src/main/java/pathfinder/ModelAPI.java) | interface representing the API that CampusMap needs to implement |
| [CampusMap](https://github.com/bellaroseee/SDI-hw-pathfinder/blob/master/src/main/java/pathfinder/CampusMap.java) | implements the ModelAPI and interacts directly with the Spark Server |
