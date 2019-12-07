package eu.steffo.cleaver.logic.split;

public class SplitBySizeConfig extends SplitConfig {
    protected int size;

    public SplitBySizeConfig(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return String.format("%d bytes", this.size);
    }
}