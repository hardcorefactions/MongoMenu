package wtf.jsexp.mongomenu.menu;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.conversions.Bson;
import wtf.jsexp.mongomenu.MongoMenu;

@Setter
@Getter
public class Menu {
    private String name;
    private String displayName;
    private Document contents;

    public Menu(String name) {
        this.name = name;
        load();
    }

    public void load() {
        Document document = MongoMenu.inst.getMenusCollection().find(Filters.eq("_id", name)).first();

        if (document == null) return;

        name        = document.getString("name");
        displayName = document.getString("displayName");
        contents    = (Document) document.get("contents");
    }

    public void save() {
        Document document = new Document("_id", name);

        document.put("name", name);
        document.put("displayName", displayName);
        document.put("contents", contents);

        Bson filter = Filters.eq("_id", name);
        FindIterable<Document> iterable = MongoMenu.inst.getMenusCollection().find(filter);

        if (iterable.first() == null) {
            MongoMenu.inst.getMenusCollection().insertOne(document);
        } else {
            MongoMenu.inst.getMenusCollection().replaceOne(filter, document);
        }
    }

    public void delete() {
        Document document = MongoMenu.inst.getMenusCollection().find(Filters.eq("_id", name)).first();

        if (document == null) return;

        MongoMenu.inst.getMenusCollection().deleteOne(document);
        MongoMenu.inst.getMenuManager().getMenus().remove(name);
    }
}