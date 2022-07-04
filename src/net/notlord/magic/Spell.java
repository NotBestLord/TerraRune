package net.notlord.magic;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Spell {
	public final String id;
	public final float manaCost;
	public final int castingTime;
	public final int cooldown;
	public Spell(String id, float manaCost, int castingTime, int cooldown) {
		this.id = id;
		this.manaCost = manaCost;
		this.castingTime = castingTime;
		this.cooldown = cooldown;
	}

	public static class ProjectileSpell extends Spell{
		private Consumer<Entity> particleEvent;
		public final int duration;
		public final float size;
		private Consumer<Entity> hitEvent;

		public ProjectileSpell(String id, float manaCost, int castingTime, int cooldown, Consumer<Entity> particleEvent, int duration, float size, Consumer<Entity> hitEvent) {
			super(id, manaCost, castingTime, cooldown);
			this.particleEvent = particleEvent;
			this.duration = duration;
			this.size = size;
			this.hitEvent = hitEvent;
		}

		public void triggerParticleEvent(Entity entity){
			if(particleEvent != null){
				particleEvent.accept(entity);
			}
		}

		public void triggerHitEvent(Entity entity){
			if(hitEvent != null){
				hitEvent.accept(entity);
			}
		}
	}

	/**
	 * @range
	 * 0 = self
	 * >0 = look entity
	 */
	public static class EffectSpell extends Spell{
		private Consumer<Entity> startEvent;
		public final int range;
		public final int duration;
		private Consumer<Entity> endEvent;

		public EffectSpell(String id, float manaCost, int castingTime, int cooldown, Consumer<Entity> startEvent, int range, int duration, Consumer<Entity> endEvent) {
			super(id, manaCost, castingTime, cooldown);
			this.startEvent = startEvent;
			this.range = range;
			this.duration = duration;
			this.endEvent = endEvent;
		}

		public void triggerStartEvent(Entity entity){
			if(startEvent != null){
				startEvent.accept(entity);
			}
		}

		public void triggerEndEvent(Entity entity){
			if(endEvent != null){
				endEvent.accept(entity);
			}
		}
	}
	public static class AlterationSpell extends Spell{
		private BiConsumer<Entity, Block> startEvent;
		public final int range;
		public final int duration;
		private BiConsumer<Entity, Block> endEvent;

		public AlterationSpell(String id, float manaCost, int castingTime, int cooldown, BiConsumer<Entity, Block> startEvent, int range, int duration, BiConsumer<Entity, Block> endEvent) {
			super(id, manaCost, castingTime, cooldown);
			this.startEvent = startEvent;
			this.range = range;
			this.duration = duration;
			this.endEvent = endEvent;
		}
		public void triggerStartEvent(Entity entity, Block block){
			if(startEvent != null){
				startEvent.accept(entity, block);
			}
		}

		public void triggerEndEvent(Entity entity, Block block){
			if(endEvent != null){
				endEvent.accept(entity, block);
			}
		}

	}
}
