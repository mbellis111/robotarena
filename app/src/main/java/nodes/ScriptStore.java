package nodes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScriptStore implements Serializable {

    private static final long serialVersionUID = 1L;
    private String scriptName;
    private List<String> contents;

    public ScriptStore() {
        scriptName = "";
        contents = new ArrayList<String>();
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public void addValue(String value) {
        this.contents.add(value);
    }
}
