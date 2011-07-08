package org.projectx.zookeeper;

import java.nio.charset.Charset;

/**
 * An object formatter which relies on the {@link Object#toString()} with a
 * configurable encoding (default of UTF-8)
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public class EncodedStringObjectFormatter implements ObjectFormatter {
  private Charset charset = Charset.forName("UTF-8");

  public void setEncoding(final String encoding) {
    this.charset = Charset.forName(encoding);
  }

  @Override
  public byte[] format(final Object object) {
    if (null == object) {
      return (byte[]) object;
    }
    return object.toString().getBytes(charset);
  }

}
