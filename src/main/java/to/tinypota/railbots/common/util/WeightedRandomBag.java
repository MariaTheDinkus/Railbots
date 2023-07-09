package to.tinypota.railbots.common.util;

import net.minecraft.entity.LivingEntity;
import to.tinypota.railbots.Railbots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WeightedRandomBag<T> {
	private final Random rand;
	private double accumulatedWeight;
	private final List<Entry> entries;
	
	public WeightedRandomBag() {
		this(new Random());
	}
	
	public WeightedRandomBag(Random rand) {
		this.rand = rand;
		accumulatedWeight = 0;
		entries = new ArrayList<>();
	}
	
	public void addEntry(T object, double weight) {
		accumulatedWeight += weight;
		var e = new Entry();
		e.object = object;
		e.accumulatedWeight = accumulatedWeight;
		entries.add(e);
	}
	
	public List<Entry> getEntries() {
		return entries;
	}
	
	public T getRandom() {
		var r = rand.nextDouble() * accumulatedWeight;
		
		for (var entry : entries) {
			if (entry.accumulatedWeight >= r) {
				return entry.object;
			}
		}
		return null; //should only happen when there are no entries
	}
	
	public T getRandom(LivingEntity entity) {
		var uuid = entity.getUuid();
		long seed = uuid.getLeastSignificantBits() ^ uuid.getMostSignificantBits();
		var rand = new Random(seed);
		Railbots.LOGGER.info("UUID: " + uuid.toString());
		Railbots.LOGGER.info("RAND SEED: " + seed);
		var r = rand.nextDouble() * accumulatedWeight;
		
		List<Entry> shuffledEntries = new ArrayList<>(entries);
		Collections.shuffle(shuffledEntries, rand);
		
		for (var entry : shuffledEntries) {
			if (entry.accumulatedWeight >= r) {
				Railbots.LOGGER.info("RETURN OBJECT: " + entry.object);
				return entry.object;
			}
		}
		return null; //should only happen when there are no entries
	}
	
	public class Entry {
		double accumulatedWeight;
		T object;
		
		public T getObject() {
			return object;
		}
	}
}
