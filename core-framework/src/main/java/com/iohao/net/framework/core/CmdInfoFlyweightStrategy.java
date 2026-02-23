package com.iohao.net.framework.core;

/**
 * Strategy options for the {@link CmdInfo} flyweight cache used by {@link CmdInfoFlyweightFactory}.
 * <ul>
 *   <li>{@link #MAP} -- concurrent hash-map; best for large or sparse command spaces.</li>
 *   <li>{@link #TWO_ARRAY} -- pre-allocated 2-D array indexed by {@code [cmd][subCmd]}.</li>
 *   <li>{@link #SPACE_FOR_TIME} -- flat array indexed by the merged command value for O(1) lookup.</li>
 * </ul>
 */
public enum CmdInfoFlyweightStrategy {
    MAP,
    TWO_ARRAY,
    SPACE_FOR_TIME
}
