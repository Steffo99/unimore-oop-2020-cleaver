package eu.steffo.cleaver.logic.split;

public class SplitByPartsConfig extends SplitConfig {
    protected int parts;

    public SplitByPartsConfig(int parts) {
        this.parts = parts;
    }

    public int getParts() {
        return parts;
    }

    @Override
    public String toString() {
        return String.format("%d parts", this.parts);
    }
}
