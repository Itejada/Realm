package com.example.realmapp;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        if(oldVersion == 1) {
            Log.d("Migration", "actualitzant a la versió 1");
            RealmObjectSchema personaSchema = schema.get("Persona");
            personaSchema.addField("apellido", String.class);
        }
    }
}
