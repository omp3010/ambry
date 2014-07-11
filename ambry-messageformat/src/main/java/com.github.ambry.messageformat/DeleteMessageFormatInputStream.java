package com.github.ambry.messageformat;

import com.github.ambry.store.StoreKey;
import java.nio.ByteBuffer;


/**
 * Represents a message that consist of the delete record.
 * This format is used to delete a blob
 *
 *  - - - - - - - - - - - - -
 * |     Message Header      |
 *  - - - - - - - - - - - - -
 * |       blob key          |
 *  - - - - - - - - - - - - -
 * |      Delete Record      |
 *  - - - - - - - - - - - - -
 *
 */
public class DeleteMessageFormatInputStream extends MessageFormatInputStream {
  public DeleteMessageFormatInputStream(StoreKey key)
      throws MessageFormatException {
    int headerSize = MessageFormatRecord.MessageHeader_Format_V1.getHeaderSize();
    int deleteRecordSize = MessageFormatRecord.Delete_Format_V1.getDeleteRecordSize();
    buffer = ByteBuffer.allocate(headerSize + key.sizeInBytes() + deleteRecordSize);
    MessageFormatRecord.MessageHeader_Format_V1
        .serializeHeader(buffer, deleteRecordSize, MessageFormatRecord.Message_Header_Invalid_Relative_Offset,
            headerSize + key.sizeInBytes(), MessageFormatRecord.Message_Header_Invalid_Relative_Offset,
            MessageFormatRecord.Message_Header_Invalid_Relative_Offset);
    buffer.put(key.toBytes());
    // set the message as deleted
    MessageFormatRecord.Delete_Format_V1.serializeDeleteRecord(buffer, true);
    messageLength = buffer.capacity();
    buffer.flip();
  }
}