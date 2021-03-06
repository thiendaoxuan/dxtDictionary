/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.vng.zing.dictionaryService.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User implements org.apache.thrift.TBase<User, User._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("User");

  private static final org.apache.thrift.protocol.TField USER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("userId", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField PASSWORDS_FIELD_DESC = new org.apache.thrift.protocol.TField("passwords", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField ROLE_FIELD_DESC = new org.apache.thrift.protocol.TField("role", org.apache.thrift.protocol.TType.I32, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new UserStandardSchemeFactory());
    schemes.put(TupleScheme.class, new UserTupleSchemeFactory());
  }

  public String userId; // required
  public String passwords; // required
  public int role; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    USER_ID((short)1, "userId"),
    PASSWORDS((short)2, "passwords"),
    ROLE((short)3, "role");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // USER_ID
          return USER_ID;
        case 2: // PASSWORDS
          return PASSWORDS;
        case 3: // ROLE
          return ROLE;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __ROLE_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private _Fields optionals[] = {_Fields.ROLE};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.USER_ID, new org.apache.thrift.meta_data.FieldMetaData("userId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PASSWORDS, new org.apache.thrift.meta_data.FieldMetaData("passwords", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ROLE, new org.apache.thrift.meta_data.FieldMetaData("role", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(User.class, metaDataMap);
  }

  public User() {
  }

  public User(
    String userId,
    String passwords)
  {
    this();
    this.userId = userId;
    this.passwords = passwords;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public User(User other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetUserId()) {
      this.userId = other.userId;
    }
    if (other.isSetPasswords()) {
      this.passwords = other.passwords;
    }
    this.role = other.role;
  }

  public User deepCopy() {
    return new User(this);
  }

  @Override
  public void clear() {
    this.userId = null;
    this.passwords = null;
    setRoleIsSet(false);
    this.role = 0;
  }

  public String getUserId() {
    return this.userId;
  }

  public User setUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public void unsetUserId() {
    this.userId = null;
  }

  /** Returns true if field userId is set (has been assigned a value) and false otherwise */
  public boolean isSetUserId() {
    return this.userId != null;
  }

  public void setUserIdIsSet(boolean value) {
    if (!value) {
      this.userId = null;
    }
  }

  public String getPasswords() {
    return this.passwords;
  }

  public User setPasswords(String passwords) {
    this.passwords = passwords;
    return this;
  }

  public void unsetPasswords() {
    this.passwords = null;
  }

  /** Returns true if field passwords is set (has been assigned a value) and false otherwise */
  public boolean isSetPasswords() {
    return this.passwords != null;
  }

  public void setPasswordsIsSet(boolean value) {
    if (!value) {
      this.passwords = null;
    }
  }

  public int getRole() {
    return this.role;
  }

  public User setRole(int role) {
    this.role = role;
    setRoleIsSet(true);
    return this;
  }

  public void unsetRole() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ROLE_ISSET_ID);
  }

  /** Returns true if field role is set (has been assigned a value) and false otherwise */
  public boolean isSetRole() {
    return EncodingUtils.testBit(__isset_bitfield, __ROLE_ISSET_ID);
  }

  public void setRoleIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ROLE_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case USER_ID:
      if (value == null) {
        unsetUserId();
      } else {
        setUserId((String)value);
      }
      break;

    case PASSWORDS:
      if (value == null) {
        unsetPasswords();
      } else {
        setPasswords((String)value);
      }
      break;

    case ROLE:
      if (value == null) {
        unsetRole();
      } else {
        setRole((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case USER_ID:
      return getUserId();

    case PASSWORDS:
      return getPasswords();

    case ROLE:
      return Integer.valueOf(getRole());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case USER_ID:
      return isSetUserId();
    case PASSWORDS:
      return isSetPasswords();
    case ROLE:
      return isSetRole();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof User)
      return this.equals((User)that);
    return false;
  }

  public boolean equals(User that) {
    if (that == null)
      return false;

    boolean this_present_userId = true && this.isSetUserId();
    boolean that_present_userId = true && that.isSetUserId();
    if (this_present_userId || that_present_userId) {
      if (!(this_present_userId && that_present_userId))
        return false;
      if (!this.userId.equals(that.userId))
        return false;
    }

    boolean this_present_passwords = true && this.isSetPasswords();
    boolean that_present_passwords = true && that.isSetPasswords();
    if (this_present_passwords || that_present_passwords) {
      if (!(this_present_passwords && that_present_passwords))
        return false;
      if (!this.passwords.equals(that.passwords))
        return false;
    }

    boolean this_present_role = true && this.isSetRole();
    boolean that_present_role = true && that.isSetRole();
    if (this_present_role || that_present_role) {
      if (!(this_present_role && that_present_role))
        return false;
      if (this.role != that.role)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(User other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    User typedOther = (User)other;

    lastComparison = Boolean.valueOf(isSetUserId()).compareTo(typedOther.isSetUserId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUserId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.userId, typedOther.userId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPasswords()).compareTo(typedOther.isSetPasswords());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPasswords()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.passwords, typedOther.passwords);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetRole()).compareTo(typedOther.isSetRole());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRole()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.role, typedOther.role);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("User(");
    boolean first = true;

    sb.append("userId:");
    if (this.userId == null) {
      sb.append("null");
    } else {
      sb.append(this.userId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("passwords:");
    if (this.passwords == null) {
      sb.append("null");
    } else {
      sb.append(this.passwords);
    }
    first = false;
    if (isSetRole()) {
      if (!first) sb.append(", ");
      sb.append("role:");
      sb.append(this.role);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (userId == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'userId' was not present! Struct: " + toString());
    }
    if (passwords == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'passwords' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class UserStandardSchemeFactory implements SchemeFactory {
    public UserStandardScheme getScheme() {
      return new UserStandardScheme();
    }
  }

  private static class UserStandardScheme extends StandardScheme<User> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, User struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // USER_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.userId = iprot.readString();
              struct.setUserIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PASSWORDS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.passwords = iprot.readString();
              struct.setPasswordsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // ROLE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.role = iprot.readI32();
              struct.setRoleIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, User struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.userId != null) {
        oprot.writeFieldBegin(USER_ID_FIELD_DESC);
        oprot.writeString(struct.userId);
        oprot.writeFieldEnd();
      }
      if (struct.passwords != null) {
        oprot.writeFieldBegin(PASSWORDS_FIELD_DESC);
        oprot.writeString(struct.passwords);
        oprot.writeFieldEnd();
      }
      if (struct.isSetRole()) {
        oprot.writeFieldBegin(ROLE_FIELD_DESC);
        oprot.writeI32(struct.role);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class UserTupleSchemeFactory implements SchemeFactory {
    public UserTupleScheme getScheme() {
      return new UserTupleScheme();
    }
  }

  private static class UserTupleScheme extends TupleScheme<User> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, User struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.userId);
      oprot.writeString(struct.passwords);
      BitSet optionals = new BitSet();
      if (struct.isSetRole()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetRole()) {
        oprot.writeI32(struct.role);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, User struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.userId = iprot.readString();
      struct.setUserIdIsSet(true);
      struct.passwords = iprot.readString();
      struct.setPasswordsIsSet(true);
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.role = iprot.readI32();
        struct.setRoleIsSet(true);
      }
    }
  }

}

