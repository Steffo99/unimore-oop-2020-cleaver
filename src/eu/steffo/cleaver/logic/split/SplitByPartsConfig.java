package eu.steffo.cleaver.logic.split;

public class SplitByPartsConfig extends SplitConfig {
    protected int parts;

    public SplitByPartsConfig(int parts) {
        this.parts = parts;
    }

    public int getParts() {
        return parts;
    }
}
