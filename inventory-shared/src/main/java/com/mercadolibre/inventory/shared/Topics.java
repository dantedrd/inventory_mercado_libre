package com.mercadolibre.inventory.shared;

public final class Topics {
  public static final String EXCHANGE = "inventory";
  public static final String RK_RESERVED = "event.inventory.reserved";
  public static final String RK_RESERVE_REJECTED = "event.inventory.reserve_rejected";
  public static final String RK_COMMITTED = "event.inventory.committed";
  public static final String RK_CANCELLED = "event.inventory.cancelled";
  public static final String RK_CMD_RESERVE = "command.inventory.reserve";
  public static final String RK_CMD_COMMIT = "command.inventory.commit";
  public static final String RK_CMD_CANCEL = "command.inventory.cancel";

  public static final String RK_ITEM_UPSERTED = "event.item.upserted";
  public static final String RK_ITEM_DELETED  = "event.item.deleted";

  private Topics() {}
}
