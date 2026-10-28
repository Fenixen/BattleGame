package cz.kalab.battlegame.common;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String resource, Long id) {
        super("%s with id %d was not found".formatted(resource, id));
    }
}
