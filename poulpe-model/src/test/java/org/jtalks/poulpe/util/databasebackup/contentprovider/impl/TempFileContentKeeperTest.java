package org.jtalks.poulpe.util.databasebackup.contentprovider.impl;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jtalks.poulpe.util.databasebackup.contentprovider.ContentProvider;
import org.jtalks.poulpe.util.databasebackup.contentprovider.util.FileWrapper;
import org.jtalks.poulpe.util.databasebackup.exceptions.ContentPersistenceException;
import org.mockito.InOrder;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

public class TempFileContentKeeperTest {
    private TempFileContentKeeper sut;
    private ContentProvider mockContentProvider;

    @BeforeMethod
    public void beforeMethod() {
        mockContentProvider = mock(ContentProvider.class);
        sut = new TempFileContentKeeper(mockContentProvider);
    }

    @Test
    public void getMimeContentTypeTest() {
        when(mockContentProvider.getMimeContentType()).thenReturn("MIME_TYPE");
        assertEquals(sut.getMimeContentType(), "MIME_TYPE");
    }

    @Test
    public void getContentFileNameExtTest() {
        when(mockContentProvider.getContentFileNameExt()).thenReturn("EXT");
        assertEquals(sut.getContentFileNameExt(), "EXT");
    }

    @Test
    public void createsTemporaryFileForContent() throws Exception {
        FileWrapper mockFileWrapper = mock(FileWrapper.class);
        OutputStream mockOutput = mock(OutputStream.class);
        InputStream mockInput = mock(InputStream.class);

        when(mockContentProvider.getContentFileNameExt()).thenReturn("EXT");
        sut = getSpyForGetInputStreamMethod(mockFileWrapper, mockOutput, mockInput);
        sut.getInputStream();

        verify(mockFileWrapper).createTempFile(anyString(), eq("EXT"));
    }

    @Test
    public void writesContentToProvidedOutput() throws Exception {
        FileWrapper mockFileWrapper = mock(FileWrapper.class);
        OutputStream mockOutput = mock(OutputStream.class);
        InputStream mockInput = mock(InputStream.class);

        sut = getSpyForGetInputStreamMethod(mockFileWrapper, mockOutput, mockInput);
        sut.getInputStream();

        verify(mockContentProvider).writeContent(mockOutput);
    }

    @Test
    public void closesOutputAfterFinishWriting() throws Exception {
        FileWrapper mockFileWrapper = mock(FileWrapper.class);
        OutputStream mockOutput = mock(OutputStream.class);
        InputStream mockInput = mock(InputStream.class);

        sut = getSpyForGetInputStreamMethod(mockFileWrapper, mockOutput, mockInput);
        sut.getInputStream();

        InOrder inOrder = inOrder(mockContentProvider, mockOutput);
        inOrder.verify(mockContentProvider).writeContent(mockOutput);
        inOrder.verify(mockOutput).flush();
        inOrder.verify(mockOutput).close();
    }

    @Test
    public void returnsInputStreamForContent() throws Exception {
        FileWrapper mockFileWrapper = mock(FileWrapper.class);
        OutputStream mockOutput = mock(OutputStream.class);
        InputStream mockInput = mock(InputStream.class);

        sut = getSpyForGetInputStreamMethod(mockFileWrapper, mockOutput, mockInput);
        assertEquals(sut.getInputStream(), mockInput);
    }

    @Test(expectedExceptions = ContentPersistenceException.class)
    public void throwsContentPersistenceExceptionIfIOError() throws Exception {
        FileWrapper mockFileWrapper = mock(FileWrapper.class);
        OutputStream mockOutput = mock(OutputStream.class);
        InputStream mockInput = mock(InputStream.class);

        doThrow(IOException.class).when(mockOutput).close();
        sut = getSpyForGetInputStreamMethod(mockFileWrapper, mockOutput, mockInput);
        sut.getInputStream();
    }

    private TempFileContentKeeper getSpyForGetInputStreamMethod(FileWrapper mockFileWrapper,
            OutputStream mockOutput, InputStream mockInput) throws Exception {
        TempFileContentKeeper spySut = spy(sut);
        stub(spySut.fileWrapper()).toReturn(mockFileWrapper);
        doReturn(mockOutput).when(spySut).newFileOutputStream(any(File.class));
        doReturn(mockInput).when(spySut).newFileInputStream(any(File.class));

        return spySut;
    }
}
