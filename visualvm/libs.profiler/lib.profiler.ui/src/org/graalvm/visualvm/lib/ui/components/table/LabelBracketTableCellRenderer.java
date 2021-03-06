/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */

package org.graalvm.visualvm.lib.ui.components.table;

import org.graalvm.visualvm.lib.ui.UIUtils;
import java.awt.*;
import javax.swing.*;


/** Enhanced Table cell rendered that paints text labels using provided text alignment
 *
 * @author Ian Formanek
 */
public class LabelBracketTableCellRenderer extends EnhancedTableCellRenderer {
    //~ Instance fields ----------------------------------------------------------------------------------------------------------

    private JLabel label1;
    private JLabel label2;
    private int digitsWidth = -1;

    //~ Constructors -------------------------------------------------------------------------------------------------------------

    /** Creates a default table cell renderer with LEADING horizontal alignment showing border when focused. */
    public LabelBracketTableCellRenderer() {
        this(JLabel.LEADING);
    }

    public LabelBracketTableCellRenderer(int horizontalAlignment) {
        this(horizontalAlignment, "(99.9%)"); //NOI18N
    }

    public LabelBracketTableCellRenderer(int horizontalAlignment, final String widestBracketText) {
        setHorizontalAlignment(horizontalAlignment);
        label1 = new JLabel("", horizontalAlignment); //NOI18N
        label2 = new JLabel("", horizontalAlignment) { //NOI18N
                public Dimension getPreferredSize() {
                    Dimension d = super.getPreferredSize();

                    if (digitsWidth == -1) {
                        digitsWidth = getFontMetrics(getFont()).stringWidth(widestBracketText);
                    }

                    if (d.width < digitsWidth) {
                        return new Dimension(digitsWidth, d.height);
                    } else {
                        return d;
                    }
                }
            };

        Font f = label2.getFont();
        label2.setFont(new Font(f.getName(), f.getStyle(), f.getSize() - 1));

        setLayout(new BorderLayout());

        if (horizontalAlignment == JLabel.LEADING) {
            add(label1, BorderLayout.WEST);
            add(label2, BorderLayout.CENTER);
        } else {
            add(label1, BorderLayout.CENTER);
            add(label2, BorderLayout.EAST);
        }

        setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 3));
    }

    //~ Methods ------------------------------------------------------------------------------------------------------------------

    public Component getTableCellRendererComponentPersistent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                             int row, int column) {
        return new LabelBracketTableCellRenderer(label1.getHorizontalAlignment()).getTableCellRendererComponent(table, value,
                                                                                                                isSelected,
                                                                                                                hasFocus, row,
                                                                                                                column);
    }

    protected void setRowForeground(Color c) {
        super.setRowForeground(c);
        label1.setForeground(c);
        label2.setForeground(UIUtils.getDisabledForeground(c));
    }

    protected void setValue(JTable table, Object value, int row, int column) {
        if (table != null) {
            setFont(table.getFont());
        }

        if (value != null) {
            String str = value.toString();
            int bracketIdx = str.lastIndexOf('('); // NOI18N

            if (bracketIdx != -1) {
                label1.setText(str.substring(0, bracketIdx));
                label2.setText(str.substring(bracketIdx));
            } else {
                label1.setText(str);
                label2.setText(""); // NOI18N
            }
        } else {
            label1.setText(""); // NOI18N
            label2.setText(""); // NOI18N
        }
    }
}
