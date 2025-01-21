using Chat.Domain.Abstractions.IServices;
using System.Security.Cryptography;
using System.Text;

namespace Chat.Infrastructure.Services;

public class SymmetricEncryptionService : IEncryptionService
{
    private readonly byte[] _key = Encoding.UTF8.GetBytes(Environment.GetEnvironmentVariable("AES_KEY") ??
                                                          throw new InvalidOperationException("AES_KEY is not set"));

    public string Encrypt(string plainText)
    {
        try
        {
            using var aes = Aes.Create();
            aes.Key = _key;
            aes.Mode = CipherMode.ECB;

            var encryptor = aes.CreateEncryptor(aes.Key, aes.IV);
            var plainBytes = Encoding.UTF8.GetBytes(plainText);
            var encryptedBytes = encryptor.TransformFinalBlock(plainBytes, 0, plainBytes.Length);

            return Convert.ToBase64String(encryptedBytes);
        }
        catch (Exception ex)
        {
            throw new Exception("Encryption failed", ex);
        }
    }

    public string Decrypt(string encryptedText)
    {
        try
        {
            using var aes = Aes.Create();
            aes.Key = _key;
            aes.Mode = CipherMode.ECB;

            var decryptor = aes.CreateDecryptor(aes.Key, aes.IV);
            var encryptedBytes = Convert.FromBase64String(encryptedText);
            var plainBytes = decryptor.TransformFinalBlock(encryptedBytes, 0, encryptedBytes.Length);

            return Encoding.UTF8.GetString(plainBytes);
        }
        catch (Exception ex)
        {
            throw new Exception("Decryption failed", ex);
        }
    }
}