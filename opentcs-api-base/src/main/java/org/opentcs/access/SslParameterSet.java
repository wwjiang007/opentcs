// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.access;

import static java.util.Objects.requireNonNull;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.io.File;
import java.io.Serializable;

/**
 * A set of parameters to be used for SSL-encrypted socket connections.
 */
public class SslParameterSet
    implements
      Serializable {

  /**
   * The default type used for truststore and keystore files.
   */
  public static final String DEFAULT_KEYSTORE_TYPE = "PKCS12";

  /**
   * The type used for keystore and truststore.
   */
  private final String keystoreType;
  /**
   * The file path of the keystore.
   */
  private final File keystoreFile;
  /**
   * The password for the keystore file.
   */
  private final String keystorePassword;
  /**
   * The file path of the truststore.
   */
  private final File truststoreFile;
  /**
   * The password for the truststore file.
   */
  private final String truststorePassword;

  /**
   * Creates a new instance.
   *
   * @param keystoreType The type used for keystore and truststore
   * @param keystoreFile The keystore file
   * @param truststoreFile The truststore file
   * @param keystorePassword The keystore file password
   * @param truststorePassword The truststore file password
   */
  public SslParameterSet(
      @Nonnull
      String keystoreType,
      @Nullable
      File keystoreFile,
      @Nullable
      String keystorePassword,
      @Nullable
      File truststoreFile,
      @Nullable
      String truststorePassword
  ) {
    this.keystoreType = requireNonNull(keystoreType, "keystoreType");
    this.keystoreFile = keystoreFile;
    this.keystorePassword = keystorePassword;
    this.truststoreFile = truststoreFile;
    this.truststorePassword = truststorePassword;
  }

  /**
   * Returns the keystoreType used to decrypt the keystore and truststore.
   *
   * @return The keystoreType used to decrypt the keystore and truststore
   */
  @Nonnull
  public String getKeystoreType() {
    return keystoreType;
  }

  /**
   * Returns the file path of the keystore file.
   *
   * @return The file path of the keystore file
   */
  @Nullable
  public File getKeystoreFile() {
    return keystoreFile;
  }

  /**
   * Returns the password for the keystore file.
   *
   * @return The password for the keystore file
   */
  @Nullable
  public String getKeystorePassword() {
    return keystorePassword;
  }

  /**
   * Returns the file path of the truststore file.
   *
   * @return The file path of the truststore file
   */
  @Nullable
  public File getTruststoreFile() {
    return truststoreFile;
  }

  /**
   * Returns the password for the truststore file.
   *
   * @return The password for the truststore file
   */
  @Nullable
  public String getTruststorePassword() {
    return truststorePassword;
  }
}
