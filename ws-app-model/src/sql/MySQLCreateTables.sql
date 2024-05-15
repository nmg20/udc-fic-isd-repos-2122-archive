DROP TABLE Reserva;
DROP TABLE Excursion;

-- --------------------------------- Excursion ------------------------------------
CREATE TABLE Excursion ( excursionId BIGINT NOT NULL AUTO_INCREMENT,
                         ciudad VARCHAR(255) COLLATE latin1_bin NOT NULL,
                         descripcion VARCHAR(1024) COLLATE latin1_bin NOT NULL,
                         fechaComienzo DATETIME NOT NULL,
                         cuotaPersona FLOAT NOT NULL,
                         numPlazas INT NOT NULL,
                         plazasDisponibles INT NOT NULL,
                         fechaAlta DATETIME NOT NULL,
                         CONSTRAINT ExcursionPk PRIMARY KEY(excursionId)) ENGINE = InnoDB;

-- --------------------------------- Reserva ------------------------------------

CREATE TABLE Reserva (  reservaId BIGINT NOT NULL AUTO_INCREMENT,
                        excursionId BIGINT NOT NULL,
                        emailUsuario VARCHAR(255) NOT NULL,
                        numPlazas INT NOT NULL,
                        tarjetaBancaria VARCHAR(16),
                        fechaReserva DATETIME NOT NULL,
                        costeTotal FLOAT NOT NULL,
                        fechaCancelacion DATETIME NULL,
                        CONSTRAINT ReservaPK PRIMARY KEY(reservaId),
                        CONSTRAINT ReservaExcursionFK FOREIGN KEY(excursionId)
                        REFERENCES Excursion(excursionId) ON DELETE CASCADE ) ENGINE = InnoDB;