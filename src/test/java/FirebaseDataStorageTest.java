/*
import com.google.cloud.firestore.Firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Map;
import java.util.HashMap;

@RunWith(JUnit4.class)
public final class FirebaseDataStorageTest {
    //FirebaseDataStorage mockFirestore = Mockito.mock(FirebaseDataStorage.class);
    //Mockito.when(mockFirestore.someMethodCallYouWantToMock()).thenReturn(something)

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private Firestore db;

    @Test
    public void testQuery() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("macroName", "NewBot");
        docData.put("creatorId", "sydney.ec@gmail.com");
        ApiFuture<DocumentReference> addedDocRef = db.collection("macros").add(docData);
    }
}*/
