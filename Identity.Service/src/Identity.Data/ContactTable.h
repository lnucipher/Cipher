#pragma once
#include <drogon/drogon.h>

// using ContactList = std::vector<std::string>;

class ContactTable
{
private:
    ContactTable() = default;
    ContactTable(const ContactTable&) = delete;
    ContactTable(ContactTable&&) = delete;
    ContactTable& operator=(const ContactTable&) = delete;
    ContactTable& operator=(ContactTable&&) = delete;

    static void create();

public:
    static std::shared_ptr<Json::Value> getLastContactsForUser(const std::string &userId,
                                                               const unsigned int contactAmount,
                                                               const unsigned int startAt);
    static std::shared_ptr<Json::Value> addNewContact(const std::string &primaryUserId,
                                                      const std::string &secondaryUserId);
    static const std::shared_ptr<std::string> getIdByContact(const std::string &userId1,
                                                             const std::string &userId2);
    static const std::shared_ptr<Json::Value> getContactById(const std::string &contactId);
    static const std::shared_ptr<std::string> updateLastInteract(const std::string &contactId,
                                                                 const std::string &timestamp);
    static const std::shared_ptr<std::string> updateLastInteract(const std::string &primaryUser,
                                                                 const std::string &secondaryUser,
                                                                 const std::string &timestamp);
    static const std::shared_ptr<Json::Value> getUserContactIds(const std::string &userId);

    static const std::shared_ptr<bool> deleteContact(const std::string &contactId);

    friend void serviceSetup();
};