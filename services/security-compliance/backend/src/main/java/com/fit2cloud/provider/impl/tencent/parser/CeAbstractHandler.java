package com.fit2cloud.provider.impl.tencent.parser;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/9  14:17}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public abstract class CeAbstractHandler extends DefaultHandler {
    private final StringBuilder text = new StringBuilder();
    private final LinkedList<String> context = new LinkedList();

    public CeAbstractHandler() {
    }

    public final void startElement(String uri, String name, String qName, Attributes attrs) {
        this.text.setLength(0);
        this.doStartElement(uri, name, qName, attrs);
        this.context.add(name);
    }

    protected abstract void doStartElement(String var1, String var2, String var3, Attributes var4);

    public final void endElement(String uri, String name, String qName) {
        this.context.removeLast();
        this.doEndElement(uri, name, qName);
    }

    protected abstract void doEndElement(String var1, String var2, String var3);

    public final void characters(char[] ch, int start, int length) {
        this.text.append(ch, start, length);
    }

    protected final String getText() {
        return this.text.toString();
    }

    protected final boolean atTopLevel() {
        return this.context.isEmpty();
    }

    protected final boolean in(String... path) {
        if (path.length != this.context.size()) {
            return false;
        } else {
            int i = 0;

            for (Iterator var3 = this.context.iterator(); var3.hasNext(); ++i) {
                String element = (String) var3.next();
                String pattern = path[i];
                if (!pattern.equals("*") && !pattern.equals(element)) {
                    return false;
                }
            }

            return true;
        }
    }
}
