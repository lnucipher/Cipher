class ContactTable
{
private:
    ContactTable() = default;
    ContactTable(const ContactTable&) = delete;
    ContactTable(ContactTable&&) = delete;
    ContactTable& operator=(const ContactTable&) = delete;
    ContactTable& operator=(ContactTable&&) = delete;

public:
    static void createContactTable();
};