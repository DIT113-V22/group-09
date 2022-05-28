package commands_processing;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandList {
    private final ArrayList<Command> commandSequence;

    public CommandList() {
        this.commandSequence = new ArrayList<>();
    }

    public Command get(int i){
        return commandSequence.get(i);
    }

    public void drop(int i){
        commandSequence.remove(i);
    }

    public void add(Command command){
        commandSequence.add(command);
    }

    public Command getLast(){
        if (commandSequence.size() == 0){
            return null;
        }
        else {
            return commandSequence.get(commandSequence.size()-1);
        }
    }

    public Command[] getNLast(int n){
        if (n > commandSequence.size()){
            return getAll();
        }
        else {
            Command[] commands = new Command[n];

            for (int i = 0; i<n; i++){
                commands[i] = get(commandSequence.size()-1-i);
            }
            return commands;
        }
    }

    public void appendSequence(Command[] commands){
        commandSequence.addAll(Arrays.asList(commands));
    }

    public Command[] getAll(){
        return commandSequence.toArray(new Command[0]);
    }

    public ArrayList<Command> getList(){
        return commandSequence;
    }

    public String toJSON(){
        String json = "[";
        StringBuilder json_builder = new StringBuilder();

        for (Command command : commandSequence){
            json_builder.append(command.toJSON()).append(",");
        }

        json += json_builder.toString();
        json = json.substring(0,json.length()-1);
        json += "]";

        return json;
    }

    public String toCSV(){
        StringBuilder csv_build = new StringBuilder("action,direction,amount\n");

        for (Command command : commandSequence){
            csv_build.append(command.toCSV());
        }
        return csv_build.substring(0,csv_build.length()-1);
    }

    public void clear(){
        commandSequence.clear();
    }

    public String toString(){
        StringBuilder output = new StringBuilder();

        for (Command command : commandSequence){
            output.append(command.toString()).append("\n");
        }
        return output.substring(0,output.length()-1);
    }
}