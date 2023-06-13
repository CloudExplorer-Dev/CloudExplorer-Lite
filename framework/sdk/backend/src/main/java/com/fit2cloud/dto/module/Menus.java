package com.fit2cloud.dto.module;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fit2cloud.dto.CeBaseObject;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menus extends CeBaseObject {

    @Serial
    private static final long serialVersionUID = 3034243310173429290L;

    private List<Menu> menus = new ArrayList<>();

    private Menus() {
    }

    public static class Builder {
        private final Menus menu;

        public Builder() {
            this.menu = new Menus();
        }

        public Builder module(@NotNull String module) {
            this.menu.setModule(module);
            if (this.menu.menus != null) {
                for (Menu menu1 : this.menu.menus) {
                    menu1.module(module);
                }
            }

            return this;
        }

        public Builder menu(Menu.Builder menuBuilder) {
            menuBuilder.module(this.menu.getModule());
            this.menu.menus.add(menuBuilder.build());
            return this;
        }

        public Menus build() {
            return menu;
        }
    }


}
