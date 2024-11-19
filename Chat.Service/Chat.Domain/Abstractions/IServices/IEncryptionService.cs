namespace Chat.Domain.Abstractions.IServices;

public interface IEncryptionService
{
    string Encrypt(string plainText);
    string Decrypt(string encryptedText);
}