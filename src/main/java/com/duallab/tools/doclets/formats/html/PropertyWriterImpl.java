/*
 * Copyright (c) 1997, 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.duallab.tools.doclets.formats.html;

import java.io.*;

import com.sun.javadoc.*;
import com.duallab.tools.doclets.formats.html.markup.*;
import com.duallab.tools.doclets.internal.toolkit.*;
import com.duallab.tools.doclets.internal.toolkit.util.*;

/**
 * Writes property documentation in HTML format.
 *
 * @author Robert Field
 * @author Atul M Dambalkar
 * @author Jamie Ho (rewrite)
 * @author Bhavesh Patel (Modified)
 */
public class PropertyWriterImpl extends AbstractMemberWriter
    implements PropertyWriter, MemberSummaryWriter {

    public PropertyWriterImpl(SubWriterHolderWriter writer, ClassDoc classdoc) {
        super(writer, classdoc);
    }

    /**
     * {@inheritDoc}
     */
    public Content getMemberSummaryHeader(ClassDoc classDoc,
            Content memberSummaryTree) {
        memberSummaryTree.addContent(HtmlConstants.START_OF_PROPERTY_SUMMARY);
        Content memberTree = writer.getMemberTreeHeader();
        writer.addSummaryHeader(this, classDoc, memberTree);
        return memberTree;
    }

    /**
     * {@inheritDoc}
     */
    public Content getFieldDetailsTreeHeader(ClassDoc classDoc,
            Content memberDetailsTree) {
        memberDetailsTree.addContent(HtmlConstants.START_OF_PROPERTY_DETAILS);
        Content fieldDetailsTree = writer.getMemberTreeHeader();
        fieldDetailsTree.addContent(writer.getMarkerAnchor("property_detail"));
        Content heading = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING,
                writer.propertyDetailsLabel);
        fieldDetailsTree.addContent(heading);
        return fieldDetailsTree;
    }

    /**
     * {@inheritDoc}
     */
    public Content getFieldDocTreeHeader(MethodDoc field,
            Content fieldDetailsTree) {
        fieldDetailsTree.addContent(
                writer.getMarkerAnchor(field.name()));
        Content fieldDocTree = writer.getMemberTreeHeader();
        Content heading = new HtmlTree(HtmlConstants.MEMBER_HEADING);
        heading.addContent(field.name().substring(0, field.name().lastIndexOf("Property")));
        fieldDocTree.addContent(heading);
        return fieldDocTree;
    }

    /**
     * {@inheritDoc}
     */
    public Content getSignature(MethodDoc field) {
        return new Comment("property signature");
    }

    /**
     * {@inheritDoc}
     */
    public void addDeprecated(MethodDoc field, Content fieldDocTree) {
    }

    /**
     * {@inheritDoc}
     */
    public void addComments(MethodDoc field, Content fieldDocTree) {
        ClassDoc holder = field.containingClass();
        if (field.inlineTags().length > 0) {
            if (holder.equals(classdoc) ||
                    (! (holder.isPublic() || Util.isLinkable(holder, configuration())))) {
                writer.addInlineComment(field, fieldDocTree);
            } else {
                Content link = new RawHtml(
                        writer.getDocLink(LinkInfoImpl.CONTEXT_FIELD_DOC_COPY,
                        holder, field,
                        holder.isIncluded() ?
                            holder.typeName() : holder.qualifiedTypeName(),
                            false));
                Content codeLink = HtmlTree.CODE(link);
                Content strong = HtmlTree.STRONG(holder.isClass()?
                   writer.descfrmClassLabel : writer.descfrmInterfaceLabel);
                strong.addContent(writer.getSpace());
                strong.addContent(codeLink);
                fieldDocTree.addContent(HtmlTree.DIV(HtmlStyle.block, strong));
                writer.addInlineComment(field, fieldDocTree);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addTags(MethodDoc field, Content fieldDocTree) {
        writer.addTagsInfo(field, fieldDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public Content getFieldDetails(Content fieldDetailsTree) {
        return getMemberTree(fieldDetailsTree);
    }

    /**
     * {@inheritDoc}
     */
    public Content getFieldDoc(Content fieldDocTree,
            boolean isLastContent) {
        return getMemberTree(fieldDocTree, isLastContent);
    }

    /**
     * Close the writer.
     */
    public void close() throws IOException {
        writer.close();
    }

    public int getMemberKind() {
        return VisibleMemberMap.PROPERTIES;
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryLabel(Content memberTree) {
        Content label = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING,
                writer.getResource("doclet.Property_Summary"));
        memberTree.addContent(label);
    }

    /**
     * {@inheritDoc}
     */
    public String getTableSummary() {
        return configuration().getText("doclet.Member_Table_Summary",
                configuration().getText("doclet.Property_Summary"),
                configuration().getText("doclet.properties"));
    }

    /**
     * {@inheritDoc}
     */
    public String getCaption() {
        return configuration().getText("doclet.Properties");
    }

    /**
     * {@inheritDoc}
     */
    public String[] getSummaryTableHeader(ProgramElementDoc member) {
        String[] header = new String[] {
            configuration().getText("doclet.Type"),
            configuration().getText("doclet.0_and_1",
                    configuration().getText("doclet.Property"),
                    configuration().getText("doclet.Description"))
        };
        return header;
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryAnchor(ClassDoc cd, Content memberTree) {
        memberTree.addContent(writer.getMarkerAnchor("property_summary"));
    }

    /**
     * {@inheritDoc}
     */
    public void addInheritedSummaryAnchor(ClassDoc cd, Content inheritedTree) {
        inheritedTree.addContent(writer.getMarkerAnchor(
                "properties_inherited_from_class_" + configuration().getClassName(cd)));
    }

    /**
     * {@inheritDoc}
     */
    public void addInheritedSummaryLabel(ClassDoc cd, Content inheritedTree) {
        Content classLink = new RawHtml(writer.getPreQualifiedClassLink(
                LinkInfoImpl.CONTEXT_MEMBER, cd, false));
        Content label = new StringContent(cd.isClass() ?
            configuration().getText("doclet.Properties_Inherited_From_Class") :
            configuration().getText("doclet.Properties_Inherited_From_Interface"));
        Content labelHeading = HtmlTree.HEADING(HtmlConstants.INHERITED_SUMMARY_HEADING,
                label);
        labelHeading.addContent(writer.getSpace());
        labelHeading.addContent(classLink);
        inheritedTree.addContent(labelHeading);
    }

    /**
     * {@inheritDoc}
     */
    protected void addSummaryLink(int context, ClassDoc cd, ProgramElementDoc member,
            Content tdSummary) {
        Content strong = HtmlTree.STRONG(new RawHtml(
                writer.getDocLink(context,
                        cd,
                        (MemberDoc) member,
                        member.name().substring(0, member.name().lastIndexOf("Property")),
                        false,
                        true)));

        Content code = HtmlTree.CODE(strong);
        tdSummary.addContent(code);
    }

    /**
     * {@inheritDoc}
     */
    protected void addInheritedSummaryLink(ClassDoc cd,
            ProgramElementDoc member, Content linksTree) {
        linksTree.addContent(new RawHtml(
                writer.getDocLink(LinkInfoImpl.CONTEXT_MEMBER, cd, (MemberDoc)member,
                ((member.name().lastIndexOf("Property") != -1) && Configuration.getJavafxJavadoc())
                        ? member.name().substring(0, member.name().length() - "Property".length())
                        : member.name(),
                false, true)));
    }

    /**
     * {@inheritDoc}
     */
    protected void addSummaryType(ProgramElementDoc member, Content tdSummaryType) {
        MethodDoc method = (MethodDoc)member;
        addModifierAndType(method, method.returnType(), tdSummaryType);
    }

    /**
     * {@inheritDoc}
     */
    protected Content getDeprecatedLink(ProgramElementDoc member) {
        return writer.getDocLink(LinkInfoImpl.CONTEXT_MEMBER,
                (MemberDoc) member, ((MethodDoc)member).qualifiedName());
    }

    /**
     * {@inheritDoc}
     */
    protected Content getNavSummaryLink(ClassDoc cd, boolean link) {
        if (link) {
            return writer.getHyperLink("", (cd == null)?
                "property_summary":
                "properties_inherited_from_class_" +
                configuration().getClassName(cd),
                writer.getResource("doclet.navProperty"));
        } else {
            return writer.getResource("doclet.navProperty");
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void addNavDetailLink(boolean link, Content liNav) {
        if (link) {
            liNav.addContent(writer.getHyperLink("", "property_detail",
                    writer.getResource("doclet.navProperty")));
        } else {
            liNav.addContent(writer.getResource("doclet.navProperty"));
        }
    }
}
