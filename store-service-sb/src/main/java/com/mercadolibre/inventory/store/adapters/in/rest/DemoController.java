package com.mercadolibre.inventory.store.adapters.in.rest;

//@RestController
//@RequestMapping("/v1/demo")
public class DemoController {
  /*private final CommandPublisher publisher;
  private final ObjectMapper om = new ObjectMapper();
  @Value("${STORE_ID:store-1}") String storeId;

  public DemoController(CommandPublisher publisher){ this.publisher = publisher; }

  @PostMapping("/reserve")
  public ResponseEntity<?> reserve(@RequestParam String sku, @RequestParam String reservationId, @RequestParam int qty) throws Exception {
    var payload = om.writeValueAsString(Map.of("sku", sku, "siteId", storeId, "reservationId", reservationId, "qty", qty));
    publisher.send(Topics.RK_CMD_RESERVE, payload);
    return ResponseEntity.accepted().build();
  }

  @PostMapping("/commit")
  public ResponseEntity<?> commit(@RequestParam String sku, @RequestParam String reservationId) throws Exception {
    var payload = om.writeValueAsString(Map.of("sku", sku, "reservationId", reservationId));
    publisher.send(Topics.RK_CMD_COMMIT, payload);
    return ResponseEntity.accepted().build();
  }

  @PostMapping("/cancel")
  public ResponseEntity<?> cancel(@RequestParam String sku, @RequestParam String reservationId, @RequestParam(required = false) String reason) throws Exception {
    var payload = om.writeValueAsString(Map.of("sku", sku, "reservationId", reservationId, "reason", reason==null?"":reason));
    publisher.send(Topics.RK_CMD_CANCEL, payload);
    return ResponseEntity.accepted().build();
  }*/
}
