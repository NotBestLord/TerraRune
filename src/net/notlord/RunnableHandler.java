package net.notlord;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunnableHandler implements Listener {
	private static class CustomTickRunnable {
		private Runnable runnable;
		private Runnable endRunnable;
		private int baseDelay;
		private int delay;
		private int ticks;
		private int id;
		private int duration;

		public CustomTickRunnable(final Runnable runnable, final int baseDelay, final int startDelay, final int id) {
			this.runnable = runnable;
			this.baseDelay = baseDelay;
			this.delay = startDelay;
			this.ticks = 0;
			this.duration = -1;
		}

		public CustomTickRunnable(final Runnable runnable, final int baseDelay, final int startDelay, final int duration, final int id) {
			this.runnable = runnable;
			this.baseDelay = baseDelay;
			this.delay = startDelay;
			this.ticks = 0;
			this.duration = duration;
			this.endRunnable = null;
		}

		public CustomTickRunnable(final Runnable runnable, final int baseDelay, final int startDelay, final int duration, final Runnable endRunnable, final int id) {
			this.runnable = runnable;
			this.baseDelay = baseDelay;
			this.delay = startDelay;
			this.ticks = 0;
			this.duration = duration;
			this.endRunnable = endRunnable;
		}

		public boolean increaseTicks() {
			if (this.duration != -1 && this.ticks == this.duration) {
				if (this.endRunnable != null) {
					this.endRunnable.run();
				}
				return true;
			}
			if (this.ticks == this.delay) {
				this.delay += this.baseDelay;
				this.ticks++;
				this.runnable.run();
				return false;
			}
			this.ticks++;
			return false;
		}

		public int getId() {
			return this.id;
		}
	}

	private static class DelayedRunnable {
		private Runnable runnable;
		private int Tick;
		private int Delay;
		private int id;

		public DelayedRunnable(final Runnable runnable, final int delay, final int id) {
			this.runnable = runnable;
			this.Tick = 0;
			this.Delay = delay;
			this.id = id;
		}

		public boolean nextTick() {
			if (this.Tick == this.Delay) {
				this.runnable.run();
				return true;
			}
			this.Tick++;
			return false;
		}

		public int getId() {
			return this.id;
		}
	}

	private final Map<Integer, Integer> Projectiles;
	private final Map<Integer, Runnable> TickableRunnables;
	private final Map<Integer, RunnableHandler.CustomTickRunnable> CustomTickableRunnables;
	private final Map<Integer, RunnableHandler.DelayedRunnable> DelayedRunnables;
	private final List<Integer> RemovedCustomTickableRunnables = new ArrayList<>();
	private final List<Integer> RemovedDelayedRunnables = new ArrayList<>();
	public RunnableHandler(Main main) {
		this.Projectiles = new HashMap<>();
		this.TickableRunnables = new HashMap<>();
		this.CustomTickableRunnables = new HashMap<>();
		this.DelayedRunnables = new HashMap<>();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
			TickableRunnables.values().forEach(Runnable::run);
			//
			new HashMap<>(CustomTickableRunnables).forEach((id, runnable) -> {
				if(runnable.increaseTicks()){
					RemovedCustomTickableRunnables.add(id);
				}
			});
			//
			new HashMap<>(DelayedRunnables).forEach((id, runnable) -> {
				if(runnable.nextTick()){
					RemovedDelayedRunnables.add(id);
				}
			});
			//
			RemovedCustomTickableRunnables.forEach(CustomTickableRunnables::remove);
			RemovedDelayedRunnables.forEach(DelayedRunnables::remove);
			//
			RemovedCustomTickableRunnables.clear();
			RemovedDelayedRunnables.clear();
		}, 1L, 1L);
	}

	@EventHandler
	private void onProjectileHit(ProjectileHitEvent event) {
		if (this.Projectiles.containsKey(event.getEntity().getEntityId())) {
			Bukkit.getScheduler().cancelTask(this.Projectiles.get(event.getEntity().getEntityId()));
			this.Projectiles.remove(event.getEntity().getEntityId());
		}
	}

	public void addProjectileRunnable(int entityID, int runnableID) {
		this.Projectiles.put(entityID, runnableID);
	}

	public Map<Integer, Integer> getProjectiles() {
		return this.Projectiles;
	}

	public int scheduleTickTask(Runnable runnable) {
		if (this.TickableRunnables.isEmpty()) {
			this.TickableRunnables.put(0, runnable);
			return 0;
		}
		Object[] ids = this.TickableRunnables.keySet().toArray();
		int id = (int)ids[ids.length - 1] + 1;
		this.TickableRunnables.put(id, runnable);
		return id;
	}

	public void cancelTickTask(int id) {
		this.TickableRunnables.remove(id);
	}

	public int scheduleRepeatingTask(Runnable runnable, int delay, int startDelay) {
		if (this.CustomTickableRunnables.isEmpty()) {
			this.CustomTickableRunnables.put(0, new RunnableHandler.CustomTickRunnable(runnable, delay, startDelay, 0));
			return 0;
		}
		Object[] ids = this.CustomTickableRunnables.keySet().toArray();
		int id = (int)ids[ids.length - 1] + 1;
		this.CustomTickableRunnables.put(id, new RunnableHandler.CustomTickRunnable(runnable, delay, startDelay, id));
		return id;
	}

	public int scheduleLimitedRepeatingTask(Runnable runnable, int delay, int startDelay, int duration) {
		if (this.CustomTickableRunnables.isEmpty()) {
			this.CustomTickableRunnables.put(0, new RunnableHandler.CustomTickRunnable(runnable, delay, startDelay, duration, 0));
			return 0;
		}
		Object[] ids = this.CustomTickableRunnables.keySet().toArray();
		int id = (int)ids[ids.length - 1] + 1;
		this.CustomTickableRunnables.put(id, new RunnableHandler.CustomTickRunnable(runnable, delay, startDelay, duration, id));
		return id;
	}

	public int scheduleLimitedRepeatingTask(Runnable runnable, int delay, int startDelay, int duration, Runnable endRunnable) {
		if (this.CustomTickableRunnables.isEmpty()) {
			this.CustomTickableRunnables.put(0, new RunnableHandler.CustomTickRunnable(runnable, delay, startDelay, duration, endRunnable, 0));
			return 0;
		}
		final Object[] ids = this.CustomTickableRunnables.keySet().toArray();
		final int id = (int)ids[ids.length - 1] + 1;
		this.CustomTickableRunnables.put(id, new RunnableHandler.CustomTickRunnable(runnable, delay, startDelay, duration, endRunnable, id));
		return id;
	}

	public void cancelRepeatingTask(int id) {
		this.CustomTickableRunnables.remove(id);
	}

	public int scheduleDelayedTask(Runnable runnable, int delay) {
		if (this.DelayedRunnables.isEmpty()) {
			this.DelayedRunnables.put(0, new RunnableHandler.DelayedRunnable(runnable, delay, 0));
			return 0;
		}
		final Object[] ids = this.DelayedRunnables.keySet().toArray();
		final int id = (int)ids[ids.length - 1] + 1;
		this.DelayedRunnables.put(id, new RunnableHandler.DelayedRunnable(runnable, delay, id));
		return id;
	}

	public void cancelDelayedTask(int id) {
		this.DelayedRunnables.remove(id);
	}
}
