package api.state;

public record CommandState(String command, VehicleState startSate,VehicleState endState) {

    @Override
    public String toString() {
        return "CommandState{" +
                "command='" + command + '\'' +
                ", startSate=" + startSate +
                ", endState=" + endState +
                '}';
    }
}

