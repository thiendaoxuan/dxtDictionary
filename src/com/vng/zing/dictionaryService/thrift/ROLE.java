/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.vng.zing.dictionaryService.thrift;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum ROLE implements org.apache.thrift.TEnum {
  ROLE_ADMIN(0),
  ROLE_USER(1),
  NO_ROLE(2);

  private final int value;

  private ROLE(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static ROLE findByValue(int value) { 
    switch (value) {
      case 0:
        return ROLE_ADMIN;
      case 1:
        return ROLE_USER;
      case 2:
        return NO_ROLE;
      default:
        return null;
    }
  }
}