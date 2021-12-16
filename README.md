# QWaypoint
QWaypoints is a location and waypoint storing system with advanced features and ultimate perfomance.
Sadly, due to Mojang's incredible ability to annoy all developers, the plugin will only run on Java 17+ servers, including on older versions of minecraft due to compliation against the 1.18 API. 
Running on older Java versions may cause unexpected errors and will not get support.


## Features
* 1.18 Support!
* Unlimited waypoints
* Public & Private waypoints for sharing cool locations!
* Waypoint tracking with advanced trackers
* A complete gui system with a per-waypoint item capability for recognition

## Commands
``/wp help ``- Show all the commands  
``/wp check <waypointName>`` - Check a certain waypoint.  
``/wp create <name>`` - Create a new waypoint
``/wp list [world]`` - List all (or some) your waypoints  
``/wp delete <waypoint> ``- Delete a waypoint  
``/wp spawn ``- Locates the spawn of the world  
``/wp nearest`` - Locates the nearest waypoint  
``/wp distance <waypointA> <waypointB>`` - Calculates the distance between two waypoints  
``/wp set <waypoint> <state> ``- Changes the state of the waypoint  
``/wp track <waypoint>`` - Tracks a waypoints
``/wp setting <setting> [value]`` - Change a per player setting
``/wp share <waypoint>`` - Shares a waypoint in chat

### Per player Settings:
* ``DeathPoints`` - Toggles deathpoints for the player (overridden if globally disabled)
* ``tracker`` - Changes the tracker for the player (Available trackers: `actionbar`, `bossbar`, `particle`)

## Roadmap
### v4.1 Update - The customizable update
The v4.1 update will include support for limiting waypoints on a per player basis, a complete new gui system,
non-hardcoded messages files and will be released within January 2022.

## Bug reports
If you find a bug, please do not leave a bad review, head over to my [Github](https://github.com/YarinQuapi/Waypoints/issues) page and I will be thrilled to assist you.

## Disclaimer
This plugin uses BStats for basic data collection. If you wish to not participate please head over to /plugins/bstats/ and disable it.
