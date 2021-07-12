package at.shortydev.bot.panel.permissions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PanelPermissionGroup  {
    
    private String uuid;
    private String name;
    private int permissions;
    private boolean managePermissions;
}
