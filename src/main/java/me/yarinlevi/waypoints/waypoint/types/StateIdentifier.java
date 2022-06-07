package me.yarinlevi.waypoints.waypoint.types;

import lombok.Getter;

public enum StateIdentifier {
    ALL("&6Total"),
    PRIVATE("&cPrivate"),
    PUBLIC("&aPublic"),
    SERVER("&bServer");

    @Getter String state;

    StateIdentifier(String state) {
        this.state = state;
    }
}
