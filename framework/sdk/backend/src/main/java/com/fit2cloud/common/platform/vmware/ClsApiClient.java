package com.fit2cloud.common.platform.vmware;

import com.vmware.content.*;
import com.vmware.content.library.Item;
import com.vmware.content.library.SubscribedItem;
import com.vmware.content.library.item.DownloadSession;
import com.vmware.content.library.item.Storage;
import com.vmware.content.library.item.UpdateSession;
import com.vmware.content.library.item.downloadsession.File;
import com.vmware.vapi.bindings.StubConfiguration;
import com.vmware.vapi.bindings.StubFactory;
import com.vmware.vcenter.iso.Image;
import com.vmware.vcenter.ovf.LibraryItem;

public class ClsApiClient {
    private final Library libraryService;
    private final LocalLibrary localLibraryService;
    private final SubscribedLibrary subscribedLibraryService;
    private final Item itemService;
    private final SubscribedItem subscribedItemService;
    private final Storage storageService;
    private final DownloadSession downloadSessionService;
    private final File downloadSessionFileService;
    private final UpdateSession updateSessionService;
    private final com.vmware.content.library.item.updatesession.File updateSessionFileService;
    private final Configuration configurationService;
    private final Type typeService;
    private final LibraryItem ovfLibraryItemService;
    private final Image isoImageService;

    public ClsApiClient(StubFactory stubFactory, StubConfiguration stubConfig) {
        this.libraryService = (Library)stubFactory.createStub(Library.class, stubConfig);
        this.localLibraryService = (LocalLibrary)stubFactory.createStub(LocalLibrary.class, stubConfig);
        this.subscribedLibraryService = (SubscribedLibrary)stubFactory.createStub(SubscribedLibrary.class, stubConfig);
        this.itemService = (Item)stubFactory.createStub(Item.class, stubConfig);
        this.subscribedItemService = (SubscribedItem)stubFactory.createStub(SubscribedItem.class, stubConfig);
        this.storageService = (Storage)stubFactory.createStub(Storage.class, stubConfig);
        this.downloadSessionService = (DownloadSession)stubFactory.createStub(DownloadSession.class, stubConfig);
        this.downloadSessionFileService = (File)stubFactory.createStub(File.class, stubConfig);
        this.updateSessionService = (UpdateSession)stubFactory.createStub(UpdateSession.class, stubConfig);
        this.updateSessionFileService = (com.vmware.content.library.item.updatesession.File)stubFactory.createStub(com.vmware.content.library.item.updatesession.File.class, stubConfig);
        this.configurationService = (Configuration)stubFactory.createStub(Configuration.class, stubConfig);
        this.typeService = (Type)stubFactory.createStub(Type.class, stubConfig);
        this.ovfLibraryItemService = (LibraryItem)stubFactory.createStub(LibraryItem.class, stubConfig);
        this.isoImageService = (Image)stubFactory.createStub(Image.class, stubConfig);
    }

    public Library libraryService() {
        return this.libraryService;
    }

    public LocalLibrary localLibraryService() {
        return this.localLibraryService;
    }

    public SubscribedLibrary subscribedLibraryService() {
        return this.subscribedLibraryService;
    }

    public Item itemService() {
        return this.itemService;
    }

    public SubscribedItem subscribedItemService() {
        return this.subscribedItemService;
    }

    public Storage storageService() {
        return this.storageService;
    }

    public DownloadSession downloadSessionService() {
        return this.downloadSessionService;
    }

    public File downloadSessionFileService() {
        return this.downloadSessionFileService;
    }

    public UpdateSession updateSession() {
        return this.updateSessionService;
    }

    public com.vmware.content.library.item.updatesession.File updateSessionFileService() {
        return this.updateSessionFileService;
    }

    public Configuration configurationService() {
        return this.configurationService;
    }

    public Type typeService() {
        return this.typeService;
    }

    public LibraryItem ovfLibraryItemService() {
        return this.ovfLibraryItemService;
    }

    public Image isoImageService() {
        return this.isoImageService;
    }
}
