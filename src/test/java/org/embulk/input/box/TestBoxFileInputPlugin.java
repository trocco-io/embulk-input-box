package org.embulk.input.box;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.embulk.config.ConfigSource;
import org.embulk.util.config.ConfigMapper;
import org.embulk.util.config.ConfigMapperFactory;
import org.junit.Test;

public class TestBoxFileInputPlugin {
  private static final ConfigMapperFactory CONFIG_MAPPER_FACTORY =
      ConfigMapperFactory.builder().addDefaultModules().build();
  private static final ConfigMapper CONFIG_MAPPER = CONFIG_MAPPER_FACTORY.createConfigMapper();

  private ConfigSource baseJwtConfig() {
    return CONFIG_MAPPER_FACTORY
        .newConfigSource()
        .set("auth_method", "jwt")
        .set("json_config", "{}")
        .set("folder_id", "1234");
  }

  @Test
  public void maximum_retries_defaults_to_3() {
    PluginTask task = CONFIG_MAPPER.map(baseJwtConfig(), PluginTask.class);
    assertEquals(3, task.getMaximumRetries());
  }

  @Test
  public void maximum_retries_accepts_explicit_value() {
    PluginTask task =
        CONFIG_MAPPER.map(baseJwtConfig().set("maximum_retries", 10), PluginTask.class);
    assertEquals(10, task.getMaximumRetries());
  }

  @Test
  public void maximum_retries_accepts_zero() {
    PluginTask task =
        CONFIG_MAPPER.map(baseJwtConfig().set("maximum_retries", 0), PluginTask.class);
    assertEquals(0, task.getMaximumRetries());
  }

  @Test
  public void stop_when_file_not_found_defaults_to_false() {
    PluginTask task = CONFIG_MAPPER.map(baseJwtConfig(), PluginTask.class);
    assertFalse(task.getStopWhenFileNotFound());
  }

  @Test
  public void stop_when_file_not_found_can_be_true() {
    PluginTask task =
        CONFIG_MAPPER.map(baseJwtConfig().set("stop_when_file_not_found", true), PluginTask.class);
    assertTrue(task.getStopWhenFileNotFound());
  }
}
