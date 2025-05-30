package com.mitchej123.hodgepodge.hax;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.world.ChunkCoordIntPair;

import org.jetbrains.annotations.NotNull;

import com.mitchej123.hodgepodge.mixins.interfaces.MutableChunkCoordIntPair;
import com.mitchej123.hodgepodge.util.ChunkPosUtil;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

public class LongChunkCoordIntPairSet implements Set<ChunkCoordIntPair> {

    private LongSet longSet = new LongOpenHashSet();

    @Override
    public int size() {
        return longSet.size();
    }

    @Override
    public boolean isEmpty() {
        return longSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof ChunkCoordIntPair chunkCoordIntPair) {
            return longSet.contains(ChunkPosUtil.toLong(chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos));
        } else if (o instanceof Long l) {
            return longSet.contains(l);
        }
        return false;
    }

    public boolean contains(long l) {
        return longSet.contains(l);
    }

    @NotNull
    @Override
    public Iterator<ChunkCoordIntPair> iterator() {
        return new ChunkCoordIntPairIterator(longSet);
    }

    public Iterator<ChunkCoordIntPair> unsafeIterator() {
        // Reuses the same ChunkCoordIntPair object for every iteration, use this when you know the code won't
        // be storing the result anywhere
        return new ChunkCoordIntPairUnsafeIterator(longSet);
    }

    public LongIterator longIterator() {
        return longSet.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        LongIterator longIterator = longSet.iterator();
        final Object[] array = new Object[longSet.size()];
        for (int i = 0; longIterator.hasNext(); i++) {
            final long pos = longIterator.nextLong();
            array[i] = new ChunkCoordIntPair(ChunkPosUtil.getPackedX(pos), ChunkPosUtil.getPackedZ(pos));
        }
        return array;
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean add(ChunkCoordIntPair chunkCoordIntPair) {
        return this.longSet.add(ChunkPosUtil.toLong(chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos));
    }

    public boolean addLong(long l) {
        return this.longSet.add(l);
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof ChunkCoordIntPair chunkCoordIntPair) {
            return longSet.remove(ChunkPosUtil.toLong(chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos));
        } else if (o instanceof Long l) {
            return longSet.remove(l);
        }
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return longSet.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends ChunkCoordIntPair> c) {
        boolean added = false;
        for (ChunkCoordIntPair chunkCoordIntPair : c) {
            added |= this.longSet.add(ChunkPosUtil.toLong(chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos));
        }
        return added;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        final MutableChunkCoordIntPair reusablePair = (MutableChunkCoordIntPair) new ChunkCoordIntPair(0, 0);
        return longSet.removeIf(
                l -> !c.contains(reusablePair.setChunkPos(ChunkPosUtil.getPackedX(l), ChunkPosUtil.getPackedZ(l))));
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        boolean removed = false;
        for (Object o : c) {
            if (o instanceof ChunkCoordIntPair chunkCoordIntPair) {
                removed |= longSet
                        .remove(ChunkPosUtil.toLong(chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos));
            } else if (o instanceof Long l) {
                removed |= longSet.remove(l);
            }
        }
        return removed;
    }

    @Override
    public void clear() {
        this.longSet.clear();
    }
}
