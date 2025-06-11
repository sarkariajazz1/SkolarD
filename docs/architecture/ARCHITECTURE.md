```mermaid
flowchart TD

%% ROOT ENTRY
App[App.java]

%% PRESENTATION (now fully modular)
subgraph Presentation
    SkolardApp[SkolardApp.java]

    subgraph AuthViews
        LoginView
        SignUpView
    end

    subgraph BookingViews
        BookingController
        BookingInputHandler
        BookingView
    end

    subgraph DashboardViews
        SupportDashboard
        TutorView
    end

    subgraph DateTime
        DateTimeLabel
    end

    subgraph FAQViews
        FAQView
    end

    subgraph MessageViews
        MessageView
    end

    subgraph PaymentViews
        PaymentView
    end

    subgraph ProfileViews
        StudentProfileView
        TutorProfileView
    end

    subgraph RatingViews
        RatingView
    end

    subgraph SessionViews
        SessionView
    end

    subgraph SupportViews
        SupportView
    end
end

%% LOGIC (same as before)
subgraph Logic
    subgraph AuthLogic
        LoginHandler
    end

    subgraph BookingLogic
        BookingHandler
        GradeComparator
        PriorityList
        TimeComparator
        TutorComparator
    end

    subgraph FAQLogic
        FAQHandler
    end

    subgraph MessageLogic
        MessageHandler
    end

    subgraph PaymentLogic
        PaymentHandler
    end

    subgraph ProfileLogic
        ProfileHandler
        ProfileCreator
        ProfileUpdater
        ProfileViewer
        DefaultProfileFormatter
        ProfileFormatter
    end

    subgraph RatingLogic
        RatingHandler
    end

    subgraph SessionLogic
        SessionHandler
        SessionAccess
        SessionBooking
        SessionManagement
    end

    subgraph SupportLogic
        SupportHandler
    end
end

%% PERSISTENCE FACTORY
subgraph PersistenceFactoryLayer
    PersistenceFactory
    PersistenceProvider
end

%% STUBS
subgraph StubPersistence
    StubFactory
    StudentStub
    TutorStub
    SessionStub
    RatingStub
    RatingRequestStub
    SupportStub
    LoginStub
    MessageStub
    FAQStub
    CardStub
end

%% SQLITE
subgraph SQLitePersistence
    ConnectionManager
    StudentDB
    TutorDB
    SessionDB
    RatingDB
    RatingRequestDB
    SupportDB
    LoginDB
    MessageDB
    FAQDB
    CardDB
end

%% CONFIG
subgraph Config
    ConfigFile[Config.java]
    EnvironmentInitializer
    DatabaseSeeder
end

%% UTILS
subgraph Utils
    CourseUtil
    GradeUtil
    PasswordUtil
    EmailUtil
    MessageUtil
    ValidationUtil
end

%% DEPENDENCY FLOW

App --> SkolardApp

SkolardApp --> LoginHandler
SkolardApp --> BookingHandler
SkolardApp --> FAQHandler
SkolardApp --> MessageHandler
SkolardApp --> PaymentHandler
SkolardApp --> ProfileHandler
SkolardApp --> RatingHandler
SkolardApp --> SessionHandler
SkolardApp --> SupportHandler

LoginView --> LoginHandler
SignUpView --> LoginHandler

BookingView --> BookingHandler
BookingController --> BookingHandler
BookingInputHandler --> BookingHandler

FAQView --> FAQHandler
MessageView --> MessageHandler
PaymentView --> PaymentHandler

StudentProfileView --> ProfileHandler
TutorProfileView --> ProfileHandler

RatingView --> RatingHandler
SessionView --> SessionHandler
SupportView --> SupportHandler

%% Handler to Persistence
LoginHandler --> PersistenceFactory
BookingHandler --> PersistenceFactory
FAQHandler --> PersistenceFactory
MessageHandler --> PersistenceFactory
PaymentHandler --> PersistenceFactory
ProfileHandler --> PersistenceFactory
RatingHandler --> PersistenceFactory
SessionHandler --> PersistenceFactory
SupportHandler --> PersistenceFactory

%% Factory Mode
PersistenceFactory --> PersistenceProvider
PersistenceProvider --> StubFactory
PersistenceProvider --> ConnectionManager

StubFactory --> StudentStub & TutorStub & SessionStub & RatingStub & RatingRequestStub
StubFactory --> SupportStub & LoginStub & MessageStub & FAQStub & CardStub

ConnectionManager --> StudentDB & TutorDB & SessionDB & RatingDB & RatingRequestDB
ConnectionManager --> SupportDB & LoginDB & MessageDB & FAQDB & CardDB

%% Internal Logic Wiring
ProfileHandler --> ProfileCreator & ProfileUpdater & ProfileViewer & DefaultProfileFormatter & ProfileFormatter
BookingHandler --> GradeComparator & PriorityList & TimeComparator & TutorComparator
SessionHandler --> SessionAccess & SessionBooking & SessionManagement

%% Util use
ProfileHandler --> ValidationUtil
RatingHandler --> CourseUtil
LoginHandler --> PasswordUtil
MessageHandler --> MessageUtil
FAQHandler --> ValidationUtil

%% App config/init
App --> EnvironmentInitializer
App --> ConfigFile
EnvironmentInitializer --> DatabaseSeeder
