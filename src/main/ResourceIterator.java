package main;

import map.buildings.Building;
import units.Unit;

import java.util.Iterator;
import java.util.Map;

public class ResourceIterator {
    private Iterator resourceIterator;
    private Map.Entry pair;
    private Map<ResourceTypes, Integer> resourceTypesIntegerHashMap;

    public ResourceIterator(Unit unit) {
        this.resourceTypesIntegerHashMap = unit.getResourceCostMap();
        resourceIterator = resourceTypesIntegerHashMap.entrySet().iterator();
    }

    public ResourceIterator(Building building, Boolean getResourceCostMap) {
        if (getResourceCostMap) {
            this.resourceTypesIntegerHashMap = building.getResourceCostMap();
        } else {
            this.resourceTypesIntegerHashMap = building.getHarvestedResourcesMap();
        }
        resourceIterator = resourceTypesIntegerHashMap.entrySet().iterator();
    }

    public boolean hasNext() {
        if (resourceIterator.hasNext()) {
            pair = (Map.Entry) resourceIterator.next();
            return true;
        } else {
            return false;
        }
    }

    public int getValue() {
        return (int) pair.getValue();
    }

    public ResourceTypes getType() {
        return (ResourceTypes) pair.getKey();
    }
}
