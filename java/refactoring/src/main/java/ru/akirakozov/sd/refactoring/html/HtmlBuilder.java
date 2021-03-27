package ru.akirakozov.sd.refactoring.html;

public class HtmlBuilder {
    StringBuilder buffer = new StringBuilder();

    public String toString() {
        StringBuilder result = new StringBuilder("<html><body>\n");
        result.append(buffer);
        result.append("</body></html>");
        return result.toString();
    }

    public void addH1(String content) {
        buffer.append("<h1>");
        buffer.append(content);
        buffer.append("</h1>\n");
    }
    public void addContent(String content) {
        buffer.append(content);
        if (!content.isEmpty()){
            buffer.append("\n");
        }
    }
}
