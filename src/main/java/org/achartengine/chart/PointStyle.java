/**
 * Copyright (C) 2009 - 2012 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.achartengine.chart;

/**
 * The chart point style enumerator.
 * ͼ����ʽö������
 * ����������ǣ�������ͼ�и��������ʽ����Բ�Σ������Σ������Σ����ε�
 */
public enum PointStyle {
  X("x"), 
  /** Բ */
  CIRCLE("circle"),
  /** ������ */
  TRIANGLE("triangle"), 
  /** �����Σ�ƽ���ģ�ֱ�ǵģ���ֱ�� */
  SQUARE("square"), 
  /** ���Σ� ��ʯ�����ʯ�������� */
  DIAMOND("diamond"), 
  /** �� */
  POINT("point");

  /** The point shape name. */
  private String mName;

  /**
   * The point style enum constructor.
   * 
   * @param name the name
   */
  private PointStyle(String name) {
    mName = name;
  }

  /**
   * Returns the point shape name.
   * 
   * @return the point shape name
   */
  public String getName() {
    return mName;
  }

  /**
   * Returns the point shape name.
   * 
   * @return the point shape name
   */
  public String toString() {
    return getName();
  }

  /**
   * Return the point shape that has the provided symbol.
   * 
   * @param name the point style name
   * @return the point shape
   */
  public static PointStyle getPointStyleForName(String name) {
    PointStyle pointStyle = null;
    PointStyle[] styles = values();
    int length = styles.length;
    for (int i = 0; i < length && pointStyle == null; i++) {
      if (styles[i].mName.equals(name)) {
        pointStyle = styles[i];
      }
    }
    return pointStyle;
  }

  /**
   * Returns the point shape index based on the given name.
   * 
   * @return the point shape index
   */
  public static int getIndexForName(String name) {
    int index = -1;
    PointStyle[] styles = values();
    int length = styles.length;
    for (int i = 0; i < length && index < 0; i++) {
      if (styles[i].mName.equals(name)) {
        index = i;
      }
    }
    return Math.max(0, index);
  }

}